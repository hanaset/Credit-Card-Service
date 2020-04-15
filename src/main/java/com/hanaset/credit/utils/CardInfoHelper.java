package com.hanaset.credit.utils;

import com.hanaset.credit.model.CardInfo;
import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CardInfoHelper {

    private final AES256Util aes256Util;

    public CardInfoHelper(AES256Util aes256Util) {
        this.aes256Util = aes256Util;
    }

    public String encrypt(CardInfo cardInfo) {

        String data = cardInfo.getCardNumber() + "-" +
                cardInfo.getCvc() + "-" +
                cardInfo.getValidDate();

        try {
            return aes256Util.encrypt(data);
        } catch (Exception e) {
            throw new CreditException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.CRYPTO_ERROR, "카드정보 암호화 과정에서 에러 발생!!");
        }
    }

    public CardInfo decrypt(String enc) {

        try {
            String dec = aes256Util.decrypt(enc);
            String[] datas = dec.split("-");
            return CardInfo.builder()
                    .cardNumber(datas[0])
                    .cvc(datas[1])
                    .validDate(datas[2])
                    .build();

        } catch (Exception e) {
            throw new CreditException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.CRYPTO_ERROR, "카드정보 복호화 과정에서 에러 발생!!");
        }
    }
}
