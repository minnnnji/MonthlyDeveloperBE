package com.monthly_developer.monthly_developer_backend.advice;

import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseMessage> httpClientErrorExceptionAdvice(HttpClientErrorException e, HttpServletRequest request){
        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setRequestPath(request.getRequestURI());
        responseMessage.setRequestResult("fail");
        responseMessage.setData("Github Oauth Error");

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
