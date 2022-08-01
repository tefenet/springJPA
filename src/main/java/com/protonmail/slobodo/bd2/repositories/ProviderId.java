package com.protonmail.slobodo.bd2.repositories;

import java.io.Serializable;

public class ProviderId implements Serializable {
    private static final long serialVersionUID =  21L;
    private Long id;
    private Long cuit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCuit() {
        return cuit;
    }

    public void setCuit(Long cuit) {
        this.cuit = cuit;
    }
}
