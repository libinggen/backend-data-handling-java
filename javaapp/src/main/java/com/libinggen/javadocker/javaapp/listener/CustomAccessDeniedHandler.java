package com.libinggen.javadocker.javaapp.listener;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        logger.error("Authorization failure: " + accessDeniedException.getMessage());
        // Respond with an error status, e.g., 403 Forbidden
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}

