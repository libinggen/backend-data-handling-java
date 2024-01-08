package com.libinggen.javadocker.javaapp.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "secret_key_1a2b3c4d5e6f";

    public static String generateToken(String username) {
        return JWT.create().withSubject(username).withIssuedAt(new Date(System.currentTimeMillis()))
                // .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 min
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000)) // 30day
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static String validateTokenAndRetrieveSubject(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            // Log the exception with its message
            logger.error("Token validation error: {}", exception.getMessage());
            // Optional: log more details or the stack trace
            // logger.error("Token validation error: ", exception);
            return null;
        }
    }
}

