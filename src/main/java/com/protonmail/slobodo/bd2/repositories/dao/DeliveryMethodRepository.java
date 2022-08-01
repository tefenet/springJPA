package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.DeliveryMethod;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeliveryMethodRepository extends CrudRepository<DeliveryMethod, Long> {
    Optional<DeliveryMethod> findByName(String name);
}
