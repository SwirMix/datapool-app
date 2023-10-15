package org.datapool.dto;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "consumer")
public class InfluxPoint {
    @Column(name = "time")
    private Instant time;
    @Column(name = "consumer")
    private String consumer;
    @Column(name = "key")
    private Long key;
    @Column(name = "cacheId")
    private String cacheId;
    @Column(name = "hash")
    private int hash;
    @Column(name = "data")
    private String data;

    private InfluxPoint(){

    }

    private InfluxPoint(DataPoolItem item, String consumer){

    }

    public Instant getTime() {
        return time;
    }

    public InfluxPoint setTime(Instant time) {
        this.time = time;
        return this;
    }

    public String getConsumer() {
        return consumer;
    }

    public InfluxPoint setConsumer(String consumer) {
        this.consumer = consumer;
        return this;
    }

    public Long getKey() {
        return key;
    }

    public InfluxPoint setKey(Long key) {
        this.key = key;
        return this;
    }

    public String getCacheId() {
        return cacheId;
    }

    public InfluxPoint setCacheId(String cacheId) {
        this.cacheId = cacheId;
        return this;
    }

    public int getHash() {
        return hash;
    }

    public InfluxPoint setHash(int hash) {
        this.hash = hash;
        return this;
    }

    public String getData() {
        return data;
    }

    public InfluxPoint setData(String data) {
        this.data = data;
        return this;
    }
}
