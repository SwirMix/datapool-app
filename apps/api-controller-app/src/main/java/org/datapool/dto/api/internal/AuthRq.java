package org.datapool.dto.api.internal;

public class AuthRq {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public AuthRq setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthRq setPassword(String password) {
        this.password = password;
        return this;
    }
}
