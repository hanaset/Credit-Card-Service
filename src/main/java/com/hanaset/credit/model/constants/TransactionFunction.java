package com.hanaset.credit.model.constants;

public enum  TransactionFunction {
    PAYMENT("PAYMENT"), CANCEL("CANCEL");

    String data;

    TransactionFunction(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
