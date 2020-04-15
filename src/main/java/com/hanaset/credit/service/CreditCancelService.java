package com.hanaset.credit.service;

import com.hanaset.credit.cache.CreditCache;
import com.hanaset.credit.convert.TransactionConverter;
import com.hanaset.credit.entity.TransactionHistoryEntity;
import com.hanaset.credit.model.TransactionData;
import com.hanaset.credit.model.constants.TransactionFunction;
import com.hanaset.credit.repository.TransactionHistoryRepository;
import com.hanaset.credit.utils.UuidGenerator;
import com.hanaset.credit.utils.VatUtil;
import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.exception.ErrorCode;
import com.hanaset.credit.web.rest.model.CancelRequest;
import com.hanaset.credit.web.rest.model.CreditResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CreditCancelService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    public CreditCancelService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public CreditResponse cancelRequest(CancelRequest request) {

        TransactionHistoryEntity originEntity = transactionHistoryRepository.findById(request.getId())
                .orElseThrow(() -> new CreditException(HttpStatus.NOT_FOUND, ErrorCode.NOT_EXIST_ID, "입력하신 관리번호는 존재히지 않습니다."));

        if(CreditCache.idContainsAndSet(request.getId())) {
            throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.ALREADY_CARD_PROCESS, "이미 거래취소 중인 관리번호입니다. 잠시후 다시 시도해주세요.");
        }

        // 원본 데이터 (결제)
        TransactionData originData = TransactionConverter.DataToTransaction(originEntity.getData());

        Optional<List<TransactionHistoryEntity>> transactionHistoryEntityListOptional =
                transactionHistoryRepository.findByBeforeId(request.getId());

        // 취소금액과 취소부가가치세에 대한 검증
        validateTransaction(request, originData, transactionHistoryEntityListOptional);

        TransactionData transactionData = TransactionData.builder()
                .id(UuidGenerator.getUuid())
                .cardInfo(originData.getCardInfo())
                .amount(request.getAmount())
                .vat(request.getVat())
                .encrypt(originData.getEncrypt())
                .installment(0)
                .beforeId(originData.getId())
                .function(TransactionFunction.CANCEL.getData())
                .empty("")
                .build();

        TransactionHistoryEntity cancelEntity = TransactionHistoryEntity.builder()
                .id(transactionData.getId())
                .beforeId(originData.getId())
                .data(TransactionConverter.transactionToData(transactionData))
                .completedDtime(ZonedDateTime.now())
                .build();

        transactionHistoryRepository.save(cancelEntity);

        CreditResponse response = CreditResponse.builder()
                .id(cancelEntity.getId())
                .amount(transactionData.getAmount())
                .vat(transactionData.getVat())
                .completedDtime(cancelEntity.getCompletedDtime())
                .build();

        // 처리가 완료 되었을 경우
        CreditCache.idSetRemove(request.getId());
        return response;
    }

    private void validateTransaction(CancelRequest request, TransactionData originData, Optional<List<TransactionHistoryEntity>> transactionHistoryEntityListOptional) {

        if(transactionHistoryEntityListOptional.isPresent()) {
            List<TransactionData> transactionData = transactionHistoryEntityListOptional.get().stream().map(transactionHistoryEntity ->
                    TransactionConverter.DataToTransaction(transactionHistoryEntity.getData())
            ).collect(Collectors.toList());

            Integer totalCancelAmount = transactionData.stream().map(TransactionData::getAmount).reduce(Integer::sum).orElse(0)
                    + request.getAmount();

            if(totalCancelAmount > originData.getAmount()) {
                CreditCache.idSetRemove(request.getId());
                throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.OVER_CANCEL_REQUEST_AMOUNT, "취소하신 금액이 결제된 금액을 초과하였습니다.");
            }

            Integer preTotalCancelVAT = transactionData.stream().map(TransactionData::getVat).reduce(Integer::sum).orElse(0);

            if(request.getVat() == null) {
                request.setVat(preTotalCancelVAT + VatUtil.calVat(request.getAmount(), request.getVat()) > originData.getVat() ?
                        originData.getVat() - preTotalCancelVAT : VatUtil.calVat(request.getAmount(), request.getVat()));
            }

            Integer totalCancelVAT = preTotalCancelVAT + request.getVat();

            if(totalCancelVAT > originData.getVat()) {
                CreditCache.idSetRemove(request.getId());
                throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.VAT_IS_BIGGER, "남은 부가가치세가 결제금액보다 큽니다.");
            }


            if(originData.getAmount() - totalCancelAmount < originData.getVat() - totalCancelVAT) {
                CreditCache.idSetRemove(request.getId());
                throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.OVER_CANCEL_REQUEST_VAT, "취소하신 부가가치세가 결제된 부가가치세를 초과하였습니다.");
            }

            log.info("취소된 금액 {} => {}", totalCancelAmount - request.getAmount(), totalCancelAmount);
            log.info("취소된 부가가치세 {} => {}.", totalCancelVAT - request.getVat(), totalCancelVAT);

        } else {

            if(request.getVat() == null) {
                request.setVat(VatUtil.calVat(request.getAmount(), request.getVat()));
            }

            if(request.getAmount() > originData.getAmount()) {
                CreditCache.idSetRemove(request.getId());
                throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.OVER_CANCEL_REQUEST_AMOUNT, "취소하신 금액이 결제된 금액을 초과하였습니다.");
            }

            if(request.getVat() > originData.getVat()) {
                CreditCache.idSetRemove(request.getId());
                throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.OVER_CANCEL_REQUEST_VAT, "취소하신 부가가치세가 결제된 부가가치세를 초과하였습니다.");
            }

            log.info("취소된 금액 {} => {}", 0, request.getAmount());
            log.info("취소된 부가가치세 {} => {}.", 0, request.getVat());
        }
    }

}
