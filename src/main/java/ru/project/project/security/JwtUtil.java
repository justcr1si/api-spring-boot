package ru.project.project.security;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret_key;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private Key getSigningKey() {
        return new HmacKey(secret_key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        JwtClaims claims = new JwtClaims();
        claims.setSubject(username);
        claims.setIssuedAtToNow();
        NumericDate numericDate = NumericDate.now();
        numericDate.addSeconds(EXPIRATION_TIME);
        claims.setExpirationTime(numericDate);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(getSigningKey());
        jws.setAlgorithmHeaderValue(HeaderParameterNames.ALGORITHM);

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            JsonWebSignature jws = new JsonWebSignature();
            jws.setCompactSerialization(token);
            jws.setKey(getSigningKey());
            return jws.verifySignature();
        } catch (JoseException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            JsonWebSignature jws = new JsonWebSignature();
            jws.setCompactSerialization(token);
            jws.setKey(getSigningKey());

            if (!jws.verifySignature()) {
                return null;
            }

            JwtClaims claims = JwtClaims.parse(jws.getPayload());
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
