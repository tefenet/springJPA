package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;
@Entity
@Document(indexName="product_on_sale")
public class ProductOnSale extends MlBdd2AbstractEntity{
    private static final long serialVersionUID = 2149457744484501629L;

    @ManyToOne
    private Provider provider;
    @ManyToOne
    private Product product;
    private Float price;
    private Date initialDate;
    private Date finalDate;

    public ProductOnSale(Product product, Provider provider, Float price, Date initialDate) {
        this.provider = provider;
        this.product = product;
        this.price = price;
        this.initialDate = initialDate;
    }

    public ProductOnSale() {
    }

    public Provider getProvider() {
        return provider;
    }

    public Product getProduct() {
        return product;
    }

    public Float getPrice() {
        return price;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
