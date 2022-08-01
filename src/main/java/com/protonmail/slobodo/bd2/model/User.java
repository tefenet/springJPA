package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"email"})})
@Document(indexName="user")
public class User extends MlBdd2AbstractEntity {
    private static final long serialVersionUID = -4688309524066816248L;

    private String fullname;
    private String password;

    @Column(unique = true)
    private String email;
    private Date dayOfBirth;

    public User(String fullname, String password, String email, Date dayOfBirth) {
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.dayOfBirth = dayOfBirth;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDayOfBirth() {
        return new Date(dayOfBirth.toGMTString());
    }

    public void setDayOfBirth(Date dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    //TODO properly implement equals() override
    @Override
    public boolean equals(Object obj) {
        return true;
    }
}
