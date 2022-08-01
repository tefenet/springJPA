package com.protonmail.slobodo.bd2.elastic_repositories;

import com.protonmail.slobodo.bd2.model.Provider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ProviderESRepository extends ElasticsearchRepository<Provider, Long> {
    Optional<Provider> findByCuit(long cuit);
}
