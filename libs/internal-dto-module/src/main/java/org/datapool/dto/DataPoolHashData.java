package org.datapool.dto;

import java.io.Serializable;
import java.util.Map;

public class DataPoolHashData implements Serializable {
    private Map data;

    public DataPoolHashData(Map data) {
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public DataPoolHashData setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}
