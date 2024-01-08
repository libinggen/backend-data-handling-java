package com.libinggen.javadocker.javaapp.listener;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SecurityEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SecurityEventListener.class);

    @EventListener
    public void handleAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        logger.error("Authentication failure: " + event.getException().getMessage());
    }

    // You can add more listeners for different types of security events as needed
}
