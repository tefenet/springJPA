package com.protonmail.slobodo.bd2.elastic_repositories;

import com.protonmail.slobodo.bd2.model.OnDeliveryPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface OnDeliveryPaymentESRepository extends ElasticsearchRepository<OnDeliveryPayment, Long> {
    Optional<OnDeliveryPayment> findByName(String name);
}
