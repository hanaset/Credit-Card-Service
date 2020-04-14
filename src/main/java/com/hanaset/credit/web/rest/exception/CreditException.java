package com.hanaset.credit.web.rest.exception;

import org.springframework.http.HttpStatus;

public class CreditException extends RuntimeException {

    private String code;
    private HttpStatus httpStatus;

    public CreditException(HttpStatus httpStatus, String code, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
