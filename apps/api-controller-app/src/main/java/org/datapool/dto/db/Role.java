package org.datapool.dto.db;

public enum Role {
    ADMIN(0),
    READER(1),
    TEAMMATE(2);

    public int role;

    Role(int num){
        this.role = num;
    }
}
