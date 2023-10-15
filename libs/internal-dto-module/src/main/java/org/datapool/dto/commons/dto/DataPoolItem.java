package org.datapool.dto.commons.dto;

import java.util.HashMap;
import java.util.Map;

public class DataPoolItem {
    private Long key = -1l;
    private Map data = new HashMap<>();
    private int hash;

    public Long getKey() {
        return key;
    }

    public DataPoolItem(){

    }

    public DataPoolItem(Long key, Map data){
        this.key = key;
        this.data = data;
        this.hash = data.hashCode();
    }

    public DataPoolItem setKey(Long key) {
        this.key = key;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public DataPoolItem setData(Map<String, Object> data) {
        this.data = data;
        this.hash = data.hashCode();
        return this;
    }
}
