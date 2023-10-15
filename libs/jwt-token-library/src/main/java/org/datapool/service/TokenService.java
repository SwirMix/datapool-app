package org.datapool.service;

public interface TokenService<V> {
    public String createGlobalToken(V token);
    public V decryptGlobalToken(String token);
}
