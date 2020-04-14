package com.hanaset.credit.convert;

import com.hanaset.credit.model.TransactionData;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionConvertTest {

    @Test
    public void transactionPaymentToDataTest() {

        TransactionData transactionData = TransactionData.builder()
                .cardNumber("1234567890123456")
                .validDate("1125")
                .cvc(777)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .adminNumber("XXXXXXXXXXXXXXXXXXXX")
                .beforeAdminNumber("")
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

        TransactionData transactionData = TransactionData.builder()
                .cardNumber("1234567890123456")
                .validDate("1125")
                .cvc(777)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .adminNumber("ZZZZZZZZZZZZZZZZZZZZ")
                .beforeAdminNumber("XXXXXXXXXXXXXXXXXXXX")
                .empty("")
                .function("CANCEL")
                .build();

        String result = TransactionConverter.transactionToData(transactionData);
        String data = "_446CANCEL____ZZZZZZZZZZZZZZZZZZZZ1234567890123456____001125777____11000000" +
                "00010000XXXXXXXXXXXXXXXXXXXXYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY"  +
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

        TransactionData transactionData = TransactionData.builder()
                .cardNumber("1234567890123456")
                .validDate("1125")
                .cvc(777)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .adminNumber("XXXXXXXXXXXXXXXXXXXX")
                .beforeAdminNumber("")
                .empty("")
                .function("PAYMENT")
                .build();

        assertEquals(result, transactionData);
    }

    @Test
    public void dataCancelToTransaction() {

        String data = "_446CANCEL____ZZZZZZZZZZZZZZZZZZZZ1234567890123456____001125777____11000000" +
                "00010000XXXXXXXXXXXXXXXXXXXXYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" +
                "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY"  +
                "____________________________________________________________________________________________________" +
                "_______________________________________________________________________" +
                "____________________________________________________________________________";

        TransactionData result = TransactionConverter.DataToTransaction(data);

        TransactionData transactionData = TransactionData.builder()
                .cardNumber("1234567890123456")
                .validDate("1125")
                .cvc(777)
                .installment(0)
                .amount(110000)
                .vat(10000)
                .encrypt("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                .adminNumber("ZZZZZZZZZZZZZZZZZZZZ")
                .beforeAdminNumber("XXXXXXXXXXXXXXXXXXXX")
                .empty("")
                .function("CANCEL")
                .build();

        assertEquals(result, transactionData);
    }
}
