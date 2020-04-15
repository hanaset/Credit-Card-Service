package com.hanaset.credit.service;

import com.hanaset.credit.convert.TransactionConverter;
import com.hanaset.credit.entity.TransactionHistoryEntity;
import com.hanaset.credit.model.AmountInfo;
import com.hanaset.credit.web.rest.model.SearchResponse;
import com.hanaset.credit.model.TransactionData;
import com.hanaset.credit.repository.TransactionHistoryRepository;
import com.hanaset.credit.utils.CardInfoHelper;
import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditSearchService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final CardInfoHelper cardInfoHelper;

    public CreditSearchService(TransactionHistoryRepository transactionHistoryRepository,
                               CardInfoHelper cardInfoHelper) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.cardInfoHelper = cardInfoHelper;
    }

    @Transactional(readOnly = true)
    public SearchResponse search(String id) {

        if(id.length() > 20) {
            throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.REQUEST_ERROR, "잘못된 관리번호 입니다.");
        }

        TransactionHistoryEntity transactionHistoryEntity = transactionHistoryRepository.findById(id)
                .orElseThrow(() -> new CreditException(HttpStatus.NOT_FOUND, ErrorCode.DATA_NOT_FOUND, "데이터를 찾을수 없습니다."));

        TransactionData transactionData = TransactionConverter.DataToTransaction(transactionHistoryEntity.getData());

        SearchResponse response = SearchResponse.builder()
                .id(transactionData.getId())
                .amountInfo(AmountInfo.builder()
                        .amount(transactionData.getAmount())
                        .vat(transactionData.getVat())
                        .build())
                .cardInfo(cardInfoHelper.decrypt(transactionData.getEncrypt()))
                .function(transactionData.getFunction())
                .completedDtime(transactionHistoryEntity.getCompletedDtime())
                .build();

        return response;
    }
}
