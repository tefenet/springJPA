package com.protonmail.slobodo.bd2.elastic_repositories;

import com.protonmail.slobodo.bd2.model.CreditCardPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.Optional;

public interface CreditCardPaymentESRepository extends ElasticsearchRepository<CreditCardPayment, Long> {

    Optional<CreditCardPayment> findByName(String name);
}
