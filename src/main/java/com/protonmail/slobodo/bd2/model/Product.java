package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Document(indexName="product")
public class Product extends MlBdd2AbstractEntity {
    private static final long serialVersionUID = 7851541522028684350L;
    private Float weigth;
    @ManyToOne
    private Category category;
    @Column(unique = true)
    private String name;
    @OneToMany(mappedBy = "product",fetch = FetchType.EAGER/*, cascade = CascadeType.ALL*/)
    private Set<ProductOnSale> productsOnSale=new HashSet<>();

    public Product(Float weigth, Category category, String name) {
        this.weigth = weigth;
        this.category = category;
        this.name = name;
    }

    public Product() {
    }
    public Product addProductOnSale(ProductOnSale productOnSale){
        productsOnSale.add(productOnSale);
        return this;
    }
    public Set<ProductOnSale> getProductsOnSale() {
        return productsOnSale;
    }

    public void setProductsOnSale(Set<ProductOnSale> productsOnSale) {
        this.productsOnSale = productsOnSale;
    }

    public Float getWeigth() {
        return weigth;
    }

    public void setWeigth(Float weigth) {
        this.weigth = weigth;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
