package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"cuit"})})
@Document(indexName="provider")
public class Provider extends MlBdd2AbstractEntity{
    private static final long serialVersionUID = -2751087961467438000L;
    private String name;

    @Column(unique = true)
    private Long cuit;

    public Provider(String name, Long cuit) {
        this.name = name;
        this.cuit = cuit;
    }

    public Provider() {
    }

    public Long getCuit() {
        return cuit;
    }

    public void setCuit(Long cuit) {
        this.cuit = cuit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
