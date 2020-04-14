package com.hanaset.credit.convert;

import com.hanaset.credit.model.CardInfo;
import com.hanaset.credit.model.TransactionData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionConvertTest {

    @Test
    public void transactionPaymentToDataTest() {

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber("1234567890123456")
                .cvc("777")
                .validDate("1125")
                .build();

        TransactionData transactionData = TransactionData.builder()
                .cardInfo(cardInfo)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .id("XXXXXXXXXXXXXXXXXXXX")
                .beforeId("")
                .empty("")
                .function("PAYMENT")
                .build();

        String result = TransactionConverter.transactionToData(transactionData);


        String data = "_446PAYMENT___XXXXXXXXXXXXXXXXXXXX1234567890123456____001125777____1100000000010000" +
                "____________________YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "____________________________________________________________________________________________________" +
                "_______________________________________________________________________" +
                "____________________________________________________________________________";

        assertEquals(result, data);
    }

    @Test
    public void transactionCancelToData() {

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber("1234567890123456")
                .cvc("777")
                .validDate("1125")
                .build();

        TransactionData transactionData = TransactionData.builder()
                .cardInfo(cardInfo)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .id("ZZZZZZZZZZZZZZZZZZZZ")
                .beforeId("XXXXXXXXXXXXXXXXXXXX")
                .empty("")
                .function("CANCEL")
                .build();

        String result = TransactionConverter.transactionToData(transactionData);
        String data = "_446CANCEL____ZZZZZZZZZZZZZZZZZZZZ1234567890123456____001125777____11000000" +
                "00010000XXXXXXXXXXXXXXXXXXXXYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "____________________________________________________________________________________________________" +
                "_______________________________________________________________________" +
                "____________________________________________________________________________";

        assertEquals(result, data);
    }

    @Test
    public void dataPaymentToTransaction() {

        String data = "_446PAYMENT___XXXXXXXXXXXXXXXXXXXX1234567890123456____001125777____1100000000010000" +
                "____________________YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "____________________________________________________________________________________________________" +
                "_______________________________________________________________________" +
                "____________________________________________________________________________";

        TransactionData result = TransactionConverter.DataToTransaction(data);

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber("1234567890123456")
                .cvc("777")
                .validDate("1125")
                .build();

        TransactionData transactionData = TransactionData.builder()
                .cardInfo(cardInfo)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .id("XXXXXXXXXXXXXXXXXXXX")
                .beforeId("")
                .empty("")
                .function("PAYMENT")
                .build();

        assertEquals(result, transactionData);
    }

    @Test
    public void dataCancelToTransaction() {

        String data = "_446CANCEL____ZZZZZZZZZZZZZZZZZZZZ1234567890123456____001125777____11000000" +
                "00010000XXXXXXXXXXXXXXXXXXXXYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "____________________________________________________________________________________________________" +
                "_______________________________________________________________________" +
                "____________________________________________________________________________";

        TransactionData result = TransactionConverter.DataToTransaction(data);

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber("1234567890123456")
                .cvc("777")
                .validDate("1125")
                .build();

        TransactionData transactionData = TransactionData.builder()
                .cardInfo(cardInfo)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .id("ZZZZZZZZZZZZZZZZZZZZ")
                .beforeId("XXXXXXXXXXXXXXXXXXXX")
                .empty("")
                .function("CANCEL")
                .build();

        assertEquals(result, transactionData);
    }
}
