package com.hanaset.credit.utils;

import com.hanaset.credit.model.CardInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CardInfoHelperTest {

    @Test
    public void helperTest() throws Exception {

        AES256Util aes256Util = new AES256Util();
        CardInfoHelper cardInfoHelper = new CardInfoHelper(aes256Util);

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber("123123123123")
                .validDate("0125")
                .cvc("777")
                .build();

        String enc = cardInfoHelper.encrypt(cardInfo);

        CardInfo result = cardInfoHelper.decrypt(enc);

        assertEquals(cardInfo.getCardNumber(), result.getCardNumber());
        assertEquals(cardInfo.getCvc(), result.getCvc());
        assertEquals(cardInfo.getValidDate(), result.getValidDate());
    }
}

