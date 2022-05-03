package org.telematix.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    public static final String USERNAME_SUBJECT = "Username";
    public static final String USERNAME_CLAIM = "username";
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(USERNAME_SUBJECT)
                .withClaim(USERNAME_CLAIM, username).sign(Algorithm.HMAC256(secret));
    }

    public String  validateTokenAndRetrieveUsername(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withSubject(USERNAME_SUBJECT).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(USERNAME_CLAIM).asString();
    }
}
