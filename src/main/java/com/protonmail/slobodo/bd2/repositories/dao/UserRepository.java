package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByEmail(String email);
    @Query(value= "select pur.client from Purchase pur group by pur.client")
    List<User> getTopNUsersMorePurchase(Pageable pageable);
}
