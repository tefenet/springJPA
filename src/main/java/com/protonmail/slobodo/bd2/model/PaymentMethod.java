package com.protonmail.slobodo.bd2.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

@Entity
@Document(indexName="payment_method")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentMethod extends MlBdd2AbstractEntity {
    private static final long serialVersionUID = 4786646661297754690L;

    protected PaymentMethod() {
    }

    //TODO properly implement equals() override
    public boolean equals(Object obj) {
        return true;
    }
}
