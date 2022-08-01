package com.protonmail.slobodo.bd2.elastic_repositories;

import com.protonmail.slobodo.bd2.model.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.Optional;

public interface CategoryESRepository extends ElasticsearchRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
