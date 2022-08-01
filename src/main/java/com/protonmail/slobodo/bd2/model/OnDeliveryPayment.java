package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;

@Entity
@Document(indexName="on_delivery_payment")
public class OnDeliveryPayment extends PaymentMethod{
    private static final long serialVersionUID = -1879187742014016528L;

    private Float promisedAmount;
    private String name;

    public OnDeliveryPayment(Float promisedAmount, String name) {
        this.promisedAmount = promisedAmount;
        this.name = name;
    }

    public OnDeliveryPayment() {
    }

    public Float getPromisedAmount() {
        return promisedAmount;
    }

    public void setPromisedAmount(Float promisedAmount) {
        this.promisedAmount = promisedAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
