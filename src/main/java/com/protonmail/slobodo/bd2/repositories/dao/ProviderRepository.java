package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.Provider;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProviderRepository extends CrudRepository<Provider, Long> {
    Optional<Provider> findByCuit(long cuit);
}
