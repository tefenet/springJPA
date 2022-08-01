package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;
@Entity
@Document(indexName="purchase")
public class Purchase extends MlBdd2AbstractEntity {
    private static final long serialVersionUID = -8621305546297328183L;

    @ManyToOne
    private ProductOnSale productOnSale;
    private Integer quantity;
    private Float amount;
    @ManyToOne
    private User client;
    @ManyToOne
    private DeliveryMethod deliveryMethod;
    @ManyToOne
    private PaymentMethod paymentMethod;

    private String address;
    private Float coordX;
    private Float coordY;
    private Date dateOfPurchase;

    public Purchase(ProductOnSale productOnSale, Integer quantity, User client, DeliveryMethod deliveryMethod, PaymentMethod paymentMethod, String address, Float coordX, Float coordY, Date dateOfPurchase) {
        this.productOnSale = productOnSale;
        this.quantity = quantity;
        this.client = client;
        this.deliveryMethod = deliveryMethod;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.coordX = coordX;
        this.coordY = coordY;
        this.dateOfPurchase = dateOfPurchase;
        amount=quantity*productOnSale.getPrice()+deliveryMethod.getCost();
    }

    public Purchase() {}

    public ProductOnSale getProductOnSale() { return productOnSale; }

    public void setProductOnSale(ProductOnSale productOnSale) { this.productOnSale = productOnSale;}

    public Integer getQuantity() { return quantity;}

    public Float getAmount() { return amount;}

    public User getClient() {
        return client;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public Float getCoordX() {
        return coordX;
    }

    public Float getCoordY() {
        return coordY;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }
}
