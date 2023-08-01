package com.example.couphoneserver.common.exception_handler;

import com.example.couphoneserver.common.exception.StoreException;
import com.example.couphoneserver.common.response.BaseErrorResponse;
import jakarta.annotation.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Priority(0)
@RestControllerAdvice
public class StoreExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StoreException.class)
    public BaseErrorResponse handle_StoreException(StoreException e) {
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }



}