package com.protonmail.slobodo.bd2.repositories.dao;


import com.protonmail.slobodo.bd2.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    List<Purchase> findByClientEmail(String userMail);
    @Query(value = "SELECT p.client FROM Purchase p inner join p.productOnSale pos inner join pos.product prod WHERE (pos.price * p.quantity) > :amount")
    List<User> findBySpendMoreThan(@Param("amount") Float amount);
    @Query(value = "SELECT c FROM Purchase p inner join p.client c inner join p.productOnSale pos inner join pos.product prod GROUP BY c HAVING (sum(pos.price * p.quantity) > :amount )")
    List<User> getUsersSpendingMoreThan(@Param("amount") double amount);
    @Query(value= "select pos.provider from Purchase pur inner join pur.productOnSale pos group by pos.provider")
    List<Provider> getTopNProvidersInPurchases(Pageable pageable);

    List<Purchase> findByDateOfPurchaseBetween(Date startDate, Date endDate);

    List<Purchase> getByProductOnSaleProviderCuit(Long cuit);

    @Query(value= "SELECT pur.productOnSale.product FROM Purchase pur GROUP BY pur.productOnSale.product")
    List<Product> getBestSellingProduct(Pageable pageable);

    List<Purchase> findByDateOfPurchase(Date date);

    @Query(value = "select distinct pos from Purchase pur inner join pur.productOnSale pos "
            + "where pur.dateOfPurchase = :day ")
    List<ProductOnSale> getSoldProductsOn(@Param("day") Date day);

    @Query(value = "SELECT dm from Purchase pur inner join pur.deliveryMethod dm group by dm.id ORDER BY COUNT(*) DESC")
    List<DeliveryMethod> getMostUsedDeliveryMethod();

    @Query(value = "SELECT pm from Purchase pur inner join pur.paymentMethod pm where pm.class = OnDeliveryPayment order by (pm.promisedAmount - pur.amount) DESC")
    List<OnDeliveryPayment> getMoreChangeOnDeliveryMethod();
}
