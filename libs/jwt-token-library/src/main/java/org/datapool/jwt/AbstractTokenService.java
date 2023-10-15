package org.datapool.jwt;

public abstract class AbstractTokenService<V>{
    protected String secret;

    public AbstractTokenService(String secret){
        this.secret = secret;
    }

    public abstract String createGlobalToken(V tokenData);

    public abstract V decryptGlobalToken(String token);
}
