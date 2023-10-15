package org.datapool.dto;

public class DataPoolHashKey {
    private int hash = -1;

    public DataPoolHashKey(){

    }

    public DataPoolHashKey(int hash){
        this.hash = hash;
    }

    public int getHash() {
        return hash;
    }

    public DataPoolHashKey setHash(int hash) {
        this.hash = hash;
        return this;
    }
}
