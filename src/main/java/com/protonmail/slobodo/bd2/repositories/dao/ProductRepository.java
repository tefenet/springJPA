package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.Category;
import com.protonmail.slobodo.bd2.model.Product;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    <S extends Product> S save(S s);

    Optional<Product> findByName(String name);

    List<Product> getByCategory(Category category);

    @Query(value = "select p from Product p ORDER BY p.weigth DESC")
    List<Product> getHeaviestProduct();

    @Query(value = "select pr from Product pr where pr.id not in"
            +" (select distinct prod.id from Purchase pur inner join pur.productOnSale pos"
            + " inner join pos.product prod )")
    List<Product> getProductsNotSold();

    @Query(value = "select c from Product p inner join p.category c group by c order by count(p) asc")
    List<Category> getCategoryWithLessProducts();
}
