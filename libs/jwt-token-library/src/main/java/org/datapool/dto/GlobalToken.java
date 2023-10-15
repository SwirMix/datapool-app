package org.datapool.dto;

public class GlobalToken {
    private String userId;
    private String email;
    private long createTimestamp;
    private long endValidTimestamp;

    public boolean equals(GlobalToken token){
        if (token.getEmail().equals(email)){
            if (token.getUserId() == userId){
                if (token.getCreateTimestamp() == createTimestamp){
                    if (token.getEndValidTimestamp() == endValidTimestamp) {
                        return true;
                    } else return false;
                } else return false;
            } else return false;
        } else return false;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public GlobalToken setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public long getEndValidTimestamp() {
        return endValidTimestamp;
    }

    public GlobalToken setEndValidTimestamp(long endValidTimestamp) {
        this.endValidTimestamp = endValidTimestamp;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GlobalToken setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public GlobalToken setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "{\"userId\":" + userId + ",\"email\": \"" + email +"\",\"createTimestamp\":" + createTimestamp +",\"endValidTimestamp\":" + endValidTimestamp +" }";
    }
}
