package com.hanaset.credit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {

    // 데이터 구분 (결제/취소)
    private String function;

    // 관리번호
    private String id;

    // 카드 정보 (카드번호, 유효날짜, cvc)
    private CardInfo cardInfo;

    // 할부
    private Integer installment;

    // 결제/취소 금액
    private Integer amount;

    // 부가가치세
    private Integer vat;

    // 원거래 관리번호 (결제시 공백)
    private String beforeId;

    // 암호화된 카드정보(카드번호, 유효기간, cvc)
    private String encrypt;

    // 예비 필드
    private String empty;
}
