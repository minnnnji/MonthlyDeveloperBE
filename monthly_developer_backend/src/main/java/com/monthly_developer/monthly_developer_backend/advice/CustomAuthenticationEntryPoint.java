package com.monthly_developer.monthly_developer_backend.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import org.bson.json.JsonObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setRequestPath(request.getRequestURI());
        responseMessage.setRequestResult("fail");
        responseMessage.setData("ROLE Denied");

        String json = new ObjectMapper().writeValueAsString(responseMessage);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json);
    }

}
