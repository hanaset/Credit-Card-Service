package com.hanaset.credit.utils;

public class MaskingUtil {

    public static String cardNumberMask(String cardNumber) {

        final Integer START_INDEX = 6;
        final Integer END_INDEX = cardNumber.length() - 3;

        String mask = "";
        for(int i = cardNumber.length() ; i > 9 ; i--) {
            mask += "*";
        }

        String result = cardNumber.substring(0, START_INDEX) + mask + cardNumber.substring(END_INDEX);

        return result;
    }
}
