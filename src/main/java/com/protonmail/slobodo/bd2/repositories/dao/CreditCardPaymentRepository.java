package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.CreditCardPayment;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface CreditCardPaymentRepository extends CrudRepository<CreditCardPayment, Long> {

    Optional<CreditCardPayment> findByName(String name);
}
