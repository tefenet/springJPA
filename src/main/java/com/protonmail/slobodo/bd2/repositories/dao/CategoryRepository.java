package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.Category;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
