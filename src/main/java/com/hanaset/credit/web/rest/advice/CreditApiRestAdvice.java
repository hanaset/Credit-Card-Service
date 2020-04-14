package com.hanaset.credit.web.rest.advice;

import com.hanaset.credit.web.rest.exception.CreditException;
import com.hanaset.credit.web.rest.exception.ErrorCode;
import com.hanaset.credit.web.rest.support.CreditApiRestSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CreditApiRestAdvice extends CreditApiRestSupport {

    @ExceptionHandler(CreditException.class)
    public ResponseEntity handleCreditException(CreditException e) {
        return exception(e.getCode(), e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError msg = e.getBindingResult().getFieldError();
        return exception(ErrorCode.REQUEST_ERROR, msg.getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}
