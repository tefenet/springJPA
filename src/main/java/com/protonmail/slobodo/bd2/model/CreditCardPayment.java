package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Document(indexName = "credit_card_payment")
public class CreditCardPayment extends PaymentMethod{
    private static final long serialVersionUID = 7013071735734104778L;

    private String brand;
    private String owner;
    private Long number;
    private Date expiry;
    private String name;
    private Integer cvv;

    public CreditCardPayment() {
    }

    public CreditCardPayment(String brand, String owner, Long number, Date expiry, String name, Integer cvv) {
        this.brand = brand;
        this.owner = owner;
        this.number = number;
        this.expiry = expiry;
        this.name = name;
        this.cvv = cvv;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }
}
