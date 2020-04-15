package com.hanaset.credit.service;

import com.hanaset.credit.cache.CreditCache;
import com.hanaset.credit.convert.TransactionConverter;
import com.hanaset.credit.entity.TransactionHistoryEntity;
import com.hanaset.credit.model.CardInfo;
import com.hanaset.credit.utils.VatUtil;
import com.hanaset.credit.web.rest.model.PaymentRequest;
import com.hanaset.credit.web.rest.model.CreditResponse;
import com.hanaset.credit.model.TransactionData;
import com.hanaset.credit.model.constants.TransactionFunction;
import com.hanaset.credit.repository.TransactionHistoryRepository;
import com.hanaset.credit.utils.CardInfoHelper;
import com.hanaset.credit.utils.UuidGenerator;
import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
public class CreditPaymentService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final CardInfoHelper cardInfoHelper;

    public CreditPaymentService(TransactionHistoryRepository transactionHistoryRepository,
                                CardInfoHelper cardInfoHelper) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.cardInfoHelper = cardInfoHelper;
    }

    public CreditResponse paymentRequest(PaymentRequest request) {

        // 이미 처리중인지 확인
        if(CreditCache.cardNumberContainsAndSet(request.getCardNumber())) {
            throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.ALREADY_CARD_PROCESS, "이미 거래요청 중인 카드 번호입니다. 잠시후 다시 시도해주세요.");
        }

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(request.getCardNumber())
                .validDate(request.getValidDate())
                .cvc(request.getCvc())
                .build();

        TransactionData transactionData = TransactionData.builder()
                .function(TransactionFunction.PAYMENT.getData())
                .id(UuidGenerator.getUuid())
                .installment(request.getInstallment())
                .cardInfo(cardInfo)
                .amount(request.getAmount())
                .vat(VatUtil.calVat(request.getAmount(), request.getVat()))
                .beforeId("")
                .encrypt(cardInfoHelper.encrypt(cardInfo))
                .empty("")
                .build();

        TransactionHistoryEntity transactionHistoryEntity = TransactionHistoryEntity.builder()
                .id(transactionData.getId())
                .beforeId("")
                .data(TransactionConverter.transactionToData(transactionData))
                .completedDtime(ZonedDateTime.now())
                .build();

        transactionHistoryRepository.save(transactionHistoryEntity);

        CreditResponse response = CreditResponse.builder()
                .id(transactionData.getId())
                .amount(transactionData.getAmount())
                .vat(transactionData.getVat())
                .completedDtime(transactionHistoryEntity.getCompletedDtime())
                .build();

        log.info("결제 완료 금액 : {}", response.getAmount());
        log.info("결제 완료 부가가치세: {}", response.getVat());

        //처리 완료 후 캐싱에서 제거
        CreditCache.cardNumberSetRemove(request.getCardNumber());

        return response;
    }

}
