package com.protonmail.slobodo.bd2.repositories;

import java.io.Serializable;

public class UserId implements Serializable {

    private static final long serialVersionUID = -9085500802240895678L;
    private Long id;
    private String email;

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserId() {
    }

    public UserId(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserId userId = (UserId) o;

        if (!getId().equals(userId.getId())) return false;
        return getEmail().equals(userId.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }
}
