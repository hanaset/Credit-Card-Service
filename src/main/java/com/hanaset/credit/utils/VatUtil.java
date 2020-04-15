package com.hanaset.credit.utils;

import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class VatUtil {

    public static Integer calVat(Integer amount, Integer vat) {

        if(vat == null) {

            Double result = amount.doubleValue() / 11;
            return Long.valueOf(Math.round(result)).intValue();

        } else {

            if (vat.compareTo(amount) == 1) {
                throw new CreditException(HttpStatus.BAD_REQUEST, ErrorCode.REQUEST_ERROR, "부가가치세가 결제금액보다 클 수 없습니다.");
            }

            return vat;
        }
    }
}
