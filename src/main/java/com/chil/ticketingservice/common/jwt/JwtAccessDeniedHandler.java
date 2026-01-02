package com.chil.ticketingservice.common.jwt;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        ExceptionCode code = ExceptionCode.ACCESS_DENIED;

        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        CommonResponse<Void> body = CommonResponse.exception(code.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
