package com.example.couphoneserver.common.exception_handler;

import com.example.couphoneserver.common.exception.CategoryException;
import com.example.couphoneserver.common.response.BaseErrorResponse;
import jakarta.annotation.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Priority(0)
@RestControllerAdvice
public class CategoryExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CategoryException.class)
    public BaseErrorResponse handle_StoreException(CategoryException e) {
        return new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
    }

}