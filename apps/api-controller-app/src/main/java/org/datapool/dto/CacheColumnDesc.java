package org.datapool.dto;

public class CacheColumnDesc {
    private String name;
    private String type;
    private String desc;

    public String getName() {
        return name;
    }

    public CacheColumnDesc setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public CacheColumnDesc setType(String type) {
        this.type = type;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public CacheColumnDesc setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
