package org.datapool.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.datapool.dto.GlobalToken;

public class GlobalTokenService extends AbstractTokenService<GlobalToken> {
    /**
     *
     * @param secret - секрет для шифровки и дешифровки
     */
    public GlobalTokenService(String secret){
        super(secret);
    }

    @Override
    public String createGlobalToken(GlobalToken token) {
        String jwtToken = JWT.create()
                .withClaim("userId", token.getUserId())
                .withClaim("email", token.getEmail())
                .withClaim("createTimestamp", token.getCreateTimestamp())
                .withClaim("endValidTimestamp", token.getEndValidTimestamp())
                .sign(Algorithm.HMAC512(secret));
        return jwtToken;
    }

    @Override
    public GlobalToken decryptGlobalToken(String token) {
        JWTVerifier verified = JWT.require(Algorithm.HMAC512(secret)).build();
        DecodedJWT jwt = verified.verify(token);
        GlobalToken globalToken = new GlobalToken()
                .setEmail(jwt.getClaim("email").asString())
                .setUserId(jwt.getClaim("userId").asString())
                .setCreateTimestamp(jwt.getClaim("createTimestamp").asLong())
                .setEndValidTimestamp(jwt.getClaim("endValidTimestamp").asLong());
        return globalToken;
    }
}
