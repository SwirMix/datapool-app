package org.datapool.dto.api.internal;

public class TeamMember {
    private String id;
    private String email;
    private String login;
    private String role;
    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public TeamMember setAdmin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public TeamMember setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getId() {
        return id;
    }

    public TeamMember(){

    }
    public TeamMember(String userId, String email, String role, String login, boolean admin){
        this.id = userId;
        this.email = email;
        this.role = role;
        this.login = login;
        this.admin = admin;
    }

    public TeamMember setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TeamMember setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRole() {
        return role;
    }

    public TeamMember setRole(String role) {
        this.role = role;
        return this;
    }
}
