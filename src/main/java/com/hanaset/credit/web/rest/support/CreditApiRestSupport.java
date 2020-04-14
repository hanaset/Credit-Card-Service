package com.hanaset.credit.web.rest.support;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class CreditApiRestSupport {

    protected static <T> ResponseEntity<?> response(T data) {
        return ResponseEntity.ok(
                ImmutableMap.of(
                        "code", "0",
                        "data", data
                )
        );
    }

    protected static <T> ResponseEntity<?> exception(String code, String msg, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                ImmutableMap.of(
                        "code", code,
                        "msg", msg
                ), httpStatus
        );
    }
}
