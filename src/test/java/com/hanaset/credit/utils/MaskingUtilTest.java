package com.hanaset.credit.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskingUtilTest {

    @Test
    public void maskingTest() {

        String cardNumber1 = "1234567890";
        String cardNumber2 = "1234567890a";
        String cardNumber3 = "1234567890ab";
        String cardNumber4 = "1234567890abc";
        String cardNumber5 = "1234567890abcd";
        String cardNumber6 = "1234567890abcde";
        String cardNumber7 = "1234567890abcdef";
        
        assertEquals(MaskingUtil.cardNumberMask(cardNumber1),"123456*890");
        assertEquals(MaskingUtil.cardNumberMask(cardNumber2),"123456**90a");
        assertEquals(MaskingUtil.cardNumberMask(cardNumber3),"123456***0ab");
        assertEquals(MaskingUtil.cardNumberMask(cardNumber4),"123456****abc");
        assertEquals(MaskingUtil.cardNumberMask(cardNumber5),"123456*****bcd");
        assertEquals(MaskingUtil.cardNumberMask(cardNumber6),"123456******cde");
        assertEquals(MaskingUtil.cardNumberMask(cardNumber7),"123456*******def");
    }
}
