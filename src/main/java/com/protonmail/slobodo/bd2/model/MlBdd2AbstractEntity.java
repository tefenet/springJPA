package com.protonmail.slobodo.bd2.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class MlBdd2AbstractEntity implements Serializable {
    private static final long serialVersionUID = 3778868462577684520L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column
    private LocalDateTime createdTimestamp;

    @Column
    private LocalDateTime modifiedTimestamp;

    @Version
    private Long version;

    @PrePersist
    public void setCreationDateTime() {
        this.createdTimestamp = LocalDateTime.now();
    }

    @PreUpdate
    public void setChangeDateTime() {
        this.modifiedTimestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public LocalDateTime getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(LocalDateTime modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
