package com.protonmail.slobodo.bd2.elastic_repositories;

import com.protonmail.slobodo.bd2.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserESRepository extends ElasticsearchRepository<User,Long> {
    Optional<User> findByEmail(String email);
    /*@Query(value= "select pur.client from Purchase pur group by pur.client")
    List<User> getTopNUsersMorePurchase(Pageable pageable);*/
}
