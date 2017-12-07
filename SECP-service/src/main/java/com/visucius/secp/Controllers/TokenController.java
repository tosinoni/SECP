package com.visucius.secp.Controllers;

import com.visucius.secp.config.SECPConfiguration;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import java.nio.charset.Charset;

public class TokenController {
    private HmacKey signatureKey;

    public TokenController (SECPConfiguration secpConfiguration) {
        signatureKey = new HmacKey(secpConfiguration.getSecretKey().getBytes(Charset.forName("UTF-8")));
    }

    public String createTokenFromUsername(String username) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setExpirationTimeMinutesInTheFuture(60 * 24);
        claims.setIssuedAtToNow();
        claims.setClaim("username", username);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(signatureKey);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA512);

        String token = jws.getCompactSerialization();
        return token;
    }

    public String getUsernameFromToken(String token) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder().setEvaluationTime(NumericDate.now())
            .setVerificationKey(signatureKey).build();
        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
        return (String) jwtClaims.getClaimValue("username");
    }
}
