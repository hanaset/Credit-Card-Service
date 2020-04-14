package com.hanaset.credit.convert;

import com.hanaset.credit.model.CardInfo;
import com.hanaset.credit.model.TransactionData;
import com.hanaset.credit.model.constants.DataLength;

public class TransactionConverter {

    public static String transactionToData(TransactionData transactionData) {

        // 처음 데이터 길이의 경우 마지막에 알 수 있음.
        String data = stringToData(transactionData.getFunction(), DataLength.DATA_FUNCTION)
                + stringToData(transactionData.getId(), DataLength.ID)
                + stringToData(transactionData.getCardInfo().getCardNumber(), DataLength.CARD_NUMBER)
                + number0ToData(transactionData.getInstallment(), DataLength.INSTALLMENT)
                + stringToData(transactionData.getCardInfo().getValidDate(), DataLength.VALID_DATE)
                + stringToData(transactionData.getCardInfo().getCvc(), DataLength.CVC)
                + numberToData(transactionData.getAmount(), DataLength.AMOUNT)
                + number0ToData(transactionData.getVat(), DataLength.VAT)
                + stringToData(transactionData.getBeforeId(), DataLength.BEFORE_ADMIN_NUMBER)
                + stringToData(transactionData.getEncrypt(), DataLength.ENCRYPT)
                + stringToData(transactionData.getEmpty(), DataLength.EMPTY);

        String size = numberToData(data.length(), DataLength.DATA_LENGTH);

        return size + data;
    }

    public static TransactionData DataToTransaction(String data) {

        Integer index = 0;
        TransactionData transactionData = new TransactionData();
        index += DataLength.DATA_LENGTH;

        transactionData.setFunction(dataToString(data.substring(index, index + DataLength.DATA_FUNCTION)));
        index += DataLength.DATA_FUNCTION;

        transactionData.setId(dataToString(data.substring(index, index + DataLength.ID)));
        index += DataLength.ID;

        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber(dataToString(data.substring(index, index + DataLength.CARD_NUMBER)));
        index += DataLength.CARD_NUMBER;

        transactionData.setInstallment(dataToNumber(data.substring(index, index + DataLength.INSTALLMENT)));
        index += DataLength.INSTALLMENT;

        cardInfo.setValidDate(dataToString(data.substring(index, index + DataLength.VALID_DATE)));
        index += DataLength.VALID_DATE;

        cardInfo.setCvc(dataToString(data.substring(index, index + DataLength.CVC)));
        index += DataLength.CVC;
        transactionData.setCardInfo(cardInfo);

        transactionData.setAmount(dataToNumber(data.substring(index, index + DataLength.AMOUNT)));
        index += DataLength.AMOUNT;

        transactionData.setVat(dataToNumber(data.substring(index, index + DataLength.VAT)));
        index += DataLength.VAT;

        transactionData.setBeforeId(dataToString(data.substring(index, index + DataLength.BEFORE_ADMIN_NUMBER)));
        index += DataLength.BEFORE_ADMIN_NUMBER;

        transactionData.setEncrypt(dataToString(data.substring(index, index + DataLength.ENCRYPT)));
        index += DataLength.ENCRYPT;

        transactionData.setEmpty(dataToString(data.substring(index)));

        return transactionData;
    }

    // 3 -> ___3
    private static String numberToData(Integer number, Integer size) {
        String strNum = String.valueOf(number);
        String empty = "";
        for (int i = 0; i < size - strNum.length(); i++) {
            empty += "_";
        }
        return empty + strNum;
    }

    // 3 -> 0003
    private static String number0ToData(Integer number, Integer size) {
        String strNum = String.valueOf(number);
        String empty = "";
        for (int i = 0; i < size - strNum.length(); i++) {
            empty += "0";
        }

        return empty + strNum;
    }

    // 3 -> 3___
    private static String numberLToData(Integer number, Integer size) {
        String strNum = String.valueOf(number);
        String empty = "";
        for (int i = 0; i < size - strNum.length(); i++) {
            empty+= "_";
        }

        return strNum + empty;
    }

    // 0003 -> 3 || ___3 -> 3 || 3___ -> 3
    private static Integer dataToNumber(String data) {

        String result = data.replace("_", "");
        return Integer.parseInt(result.length() > 0 ? result : "0");
    }


    private static String stringToData(String str, Integer size) {

        String empty = "";
        for(int i = 0 ; i < size - str.length() ; i++) {
            empty += "_";
        }

        return str + empty;
    }

    private static String dataToString(String data) {
        return data.replace("_", "");
    }
}
