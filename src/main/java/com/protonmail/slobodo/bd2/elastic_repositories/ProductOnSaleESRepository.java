package com.protonmail.slobodo.bd2.elastic_repositories;

import com.protonmail.slobodo.bd2.model.Product;
import com.protonmail.slobodo.bd2.model.ProductOnSale;
import com.protonmail.slobodo.bd2.model.Provider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;

public interface ProductOnSaleESRepository extends ElasticsearchRepository<ProductOnSale, Long> {
    List<ProductOnSale> findByProductAndProvider(Product product, Provider provider);
    /*@Query(value="SELECT pos.product FROM ProductOnSale pos")
    List<Product> getMoreExpensiveProducts(Pageable pageable);

    @Query(value="SELECT pos.product FROM ProductOnSale pos group by pos.product having count(pos)=1")
    List<Product> getProductsOnePrice();
*/
    List<ProductOnSale> getTopByOrderByPrice();

    List<ProductOnSale> findByProviderNotIn(Collection<Provider> providers);
}
