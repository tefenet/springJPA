package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;
import javax.persistence.Entity;

@Entity
@Document(indexName = "category")
public class Category extends MlBdd2AbstractEntity {
    private static final long serialVersionUID = -7192521842766800200L;

    private String name;

    public Category(String name) {
        this.name=name;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
