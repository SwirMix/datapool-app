package org.datapool.metrics;

import org.datapool.dto.DatapoolStrategy;
import org.datapool.dto.metadata.CacheMetadataKey;

public class CacheConsumer {
    private CacheMetadataKey cache;
    private String consumer;
    private DatapoolStrategy strategy;
    private boolean success;
    private int hash;
    private Long key;

    public Long getKey() {
        return key;
    }

    public CacheConsumer setKey(Long key) {
        this.key = key;
        return this;
    }

    public int getHash() {
        return hash;
    }

    public CacheConsumer setHash(int hash) {
        this.hash = hash;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public CacheConsumer setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public DatapoolStrategy getStrategy() {
        return strategy;
    }

    public CacheConsumer setStrategy(DatapoolStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public CacheMetadataKey getCache() {
        return cache;
    }

    public CacheConsumer setCache(CacheMetadataKey cache) {
        this.cache = cache;
        return this;
    }

    public String getConsumer() {
        return consumer;
    }

    public CacheConsumer setConsumer(String consumer) {
        this.consumer = consumer;
        return this;
    }
}
