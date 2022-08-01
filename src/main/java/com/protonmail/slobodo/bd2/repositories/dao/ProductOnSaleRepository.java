package com.protonmail.slobodo.bd2.repositories.dao;

import com.protonmail.slobodo.bd2.model.Product;
import com.protonmail.slobodo.bd2.model.ProductOnSale;
import com.protonmail.slobodo.bd2.model.Provider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface ProductOnSaleRepository extends PagingAndSortingRepository<ProductOnSale, Long> {
    List<ProductOnSale> findByProductAndProvider(Product product, Provider provider);
    @Query(value="SELECT pos.product FROM ProductOnSale pos")
    List<Product> getMoreExpensiveProducts(Pageable pageable);

    @Query(value="SELECT pos.product FROM ProductOnSale pos group by pos.product having count(pos)=1")
    List<Product> getProductsOnePrice();

    List<ProductOnSale> getTopByOrderByPrice();

    List<ProductOnSale> findByProviderNotIn(Collection<Provider> providers);
}
