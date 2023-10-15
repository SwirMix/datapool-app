package org.datapool.dto.api.internal;

public class UpdateUser {
    private String id;
    private String email;
    private String login;
    private String password;
    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public UpdateUser setAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public String getId() {
        return id;
    }

    public UpdateUser setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UpdateUser setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UpdateUser setPassword(String password) {
        this.password = password;
        return this;
    }
}
