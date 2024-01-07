package com.libinggen.javadocker.javaapp.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "secret_key_1a2b3c4d5e6f";

    public static String generateToken(String username) {
        return JWT.create().withSubject(username).withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutes
                                                                                      // expiration
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static String validateTokenAndRetrieveSubject(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return null;
        }
    }
}

