package org.datapool.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.datapool.dto.Passwd;

import java.util.Date;

public class PasswordJwtService extends AbstractTokenService<Passwd> {
    public PasswordJwtService(String secret) {
        super(secret);
    }

    @Override
    public String createGlobalToken(Passwd passwd) {
        String jwtToken = JWT.create()
                .withClaim("password", passwd.getPassword())
                .withClaim("timestamp", passwd.getTimestamp().getTime())
                .sign(Algorithm.HMAC512(secret));
        return jwtToken;
    }



    @Override
    public Passwd decryptGlobalToken(String token) {
        JWTVerifier verified = JWT.require(Algorithm.HMAC512(secret)).build();
        DecodedJWT jwt = verified.verify(token);
        Passwd globalToken = new Passwd()
                .setPassword(jwt.getClaim("password").asString())
                .setTimestamp(new Date(jwt.getClaim("timestamp").asLong()));
        return globalToken;
    }
}
