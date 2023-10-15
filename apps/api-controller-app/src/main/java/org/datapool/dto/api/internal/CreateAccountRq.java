package org.datapool.dto.api.internal;

public class CreateAccountRq {
    private String login;
    private String password;
    private String email;

    public String getLogin() {
        return login;
    }

    public CreateAccountRq setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CreateAccountRq setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateAccountRq setEmail(String email) {
        this.email = email;
        return this;
    }
}
