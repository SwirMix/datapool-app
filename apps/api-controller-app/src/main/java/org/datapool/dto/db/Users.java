package org.datapool.dto.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "users")
public class Users {
    @Id
    private String id;
    @Column
    private String email;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private boolean active;
    @Column
    private Date registration;
    @Column(name = "global_admin")
    private boolean globalAdmin;

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public Users setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
        return this;
    }

    public Date getRegistration() {
        return registration;
    }

    public Users setRegistration(Date registration) {
        this.registration = registration;
        return this;
    }

    public String getId() {
        return id;
    }

    public Users setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Users setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Users setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Users setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Users setActive(boolean active) {
        this.active = active;
        return this;
    }
}
