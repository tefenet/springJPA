package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.OnDeliveryPayment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OnDeliveryPaymentRepository extends CrudRepository<OnDeliveryPayment, Long> {
    Optional<OnDeliveryPayment> findByName(String name);
}
