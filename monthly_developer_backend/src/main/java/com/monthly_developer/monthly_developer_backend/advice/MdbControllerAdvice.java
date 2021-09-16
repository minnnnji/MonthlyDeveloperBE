package com.monthly_developer.monthly_developer_backend.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class MdbControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public String httpClientErrorExceptionAdvice(HttpClientErrorException e){
        return "인증 오류";
    }
}
