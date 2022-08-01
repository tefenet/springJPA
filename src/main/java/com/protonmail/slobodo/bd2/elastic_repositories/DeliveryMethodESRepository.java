package com.protonmail.slobodo.bd2.elastic_repositories;

import com.protonmail.slobodo.bd2.model.DeliveryMethod;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface DeliveryMethodESRepository extends ElasticsearchRepository<DeliveryMethod, Long> {
    Optional<DeliveryMethod> findByName(String name);
}
