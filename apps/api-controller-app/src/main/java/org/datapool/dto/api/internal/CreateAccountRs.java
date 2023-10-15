package org.datapool.dto.api.internal;

public class CreateAccountRs {
    private String id;
    private String login;
    private String email;

    public String getId() {
        return id;
    }

    public CreateAccountRs setId(String id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public CreateAccountRs setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateAccountRs setEmail(String email) {
        this.email = email;
        return this;
    }
}
