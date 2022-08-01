package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;

@Entity
@Document(indexName="delivery_method")
public class DeliveryMethod extends MlBdd2AbstractEntity {
    private static final long serialVersionUID = 1130168405438912030L;
    private Float cost;
    private Float endWeight;
    private String name;
    private Float startWeight;

    public DeliveryMethod(String name, Float cost, Float endWeight, Float startWeight) {
        this.name = name;
        this.cost = cost;
        this.endWeight = endWeight;
        this.startWeight = startWeight;
    }

    public DeliveryMethod() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getEndWeight() {
        return endWeight;
    }

    public void setEndWeight(Float endWeight) {
        this.endWeight = endWeight;
    }

    public Float getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(Float startWeight) {
        this.startWeight = startWeight;
    }

    //TODO properly implement equals() override
    public boolean equals(Object obj) {
        return true;
    }
}
