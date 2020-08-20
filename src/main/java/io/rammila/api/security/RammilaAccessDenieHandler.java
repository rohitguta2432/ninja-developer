package io.rammila.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("accessDeniedHandler")
public class RammilaAccessDenieHandler implements AccessDeniedHandler {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse servletResponse, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String response = "Access denied for resource :" + request.getRequestURI();
        servletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        servletResponse.setContentType("application/json;charset=UTF-8");
        servletResponse.setStatus(403);
        servletResponse.getWriter().write(OBJECT_MAPPER.writeValueAsString(response));

    }
}
