package org.datapool.dto;


import java.util.HashMap;
import java.util.Map;

public class DataPoolItem {
    private Long key = -1l;
    private int hash = -1;
    private Map<String, Object> data = new HashMap<>();

    public DataPoolItem(){

    }

    public Map<String, Object> getData() {
        return data;
    }

    public DataPoolItem setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public int getHash() {
        return hash;
    }

    public DataPoolItem setHash(int hash) {
        this.hash = hash;
        return this;
    }

    public Long getKey() {
        return key;
    }

    public DataPoolItem(long key, Integer hash){
        this.hash = hash;
        this.key = key;
    }

    public DataPoolItem setKey(Long key) {
        this.key = key;
        return this;
    }


}
