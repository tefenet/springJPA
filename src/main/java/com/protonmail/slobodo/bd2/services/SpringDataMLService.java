package com.protonmail.slobodo.bd2.services;


import com.protonmail.slobodo.bd2.repositories.MLException;
import com.protonmail.slobodo.bd2.model.*;
import com.protonmail.slobodo.bd2.repositories.dao.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import com.protonmail.slobodo.bd2.repositories.MLRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpringDataMLService implements MLService  {

    private final MLRepository mlRepository= new MLRepository();
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private ProductOnSaleRepository productOnSaleRepository;
    @Autowired
    private CreditCardPaymentRepository creditCardPaymentRepository;
    @Autowired
    private DeliveryMethodRepository deliveryMethodRepository;
    @Autowired
    private OnDeliveryPaymentRepository onDeliveryPaymentRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;

    private <T extends MlBdd2AbstractEntity> MlBdd2AbstractEntity create(CrudRepository<T,Long> repository, T o){
        try {
            o = repository.save(o);
        }catch (DataIntegrityViolationException e){
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t != null) {
                throw new MLException("Constraint Violation");
            }
        }
        var optionalT= repository.findById(o.getId());
        if (optionalT.isPresent()) {
            return optionalT.get();
        } else {
            throw new MLException(String.format("%s not found", o.getClass().getSimpleName()));
        }
    }

    @Override
    public Category createCategory(String name) throws MLException {
        return (Category) create(categoryRepository,new Category(name));
    }

    @Override
    public Product createProduct(String name, Float weight, Category category) throws MLException {
        return (Product) create(productRepository,new Product(weight,category,name));
    }

    @Override
    @Transactional
    public User createUser(String email, String fullname, String password, Date dayOfBirth) throws MLException {
        return (User) create(userRepository,new User(fullname, password, email ,dayOfBirth));
    }

    @Override
    public Provider createProvider(String name, Long cuit) throws MLException {
        return (Provider) create(providerRepository,new Provider(name, cuit));
    }

    @Override
    public DeliveryMethod createDeliveryMethod(String name, Float cost, Float startWeight, Float endWeight) throws MLException {
        return (DeliveryMethod) create(deliveryMethodRepository,new DeliveryMethod(name, cost, endWeight, startWeight));
    }

    @Override
    public CreditCardPayment createCreditCardPayment(String name, String brand, Long number, Date expiry, Integer cvv, String owner) throws MLException {
        return (CreditCardPayment) create(creditCardPaymentRepository ,new CreditCardPayment(brand, owner, number, expiry, name, cvv));
    }

    @Override
    public OnDeliveryPayment createOnDeliveryPayment(String name, Float promisedAmount) throws MLException {
        return (OnDeliveryPayment) create(onDeliveryPaymentRepository, new OnDeliveryPayment(promisedAmount, name));
    }

    @Override @Transactional
    public ProductOnSale createProductOnSale(Product product, Provider provider, Float price, Date initialDate) throws MLException {
        var pOnSale= new ProductOnSale(product, provider, price, initialDate);
        var pOnSaleList = productOnSaleRepository.findByProductAndProvider(product, provider);
        var lastProductOnSale= pOnSaleList.stream().max(Comparator.comparing(ProductOnSale::getInitialDate));
        if (lastProductOnSale.isEmpty() || lastProductOnSale.get().getInitialDate().before(initialDate)) {
            pOnSale = productOnSaleRepository.save(pOnSale);
            productRepository.save(product.addProductOnSale(pOnSale));
        }
        else {
            throw new MLException("Ya existe un precio para el producto con fecha de inicio de vigencia posterior a la fecha de inicio dada");
        }
        return productOnSaleRepository.findById(pOnSale.getId()).get();
    }

    @Override
    public Purchase createPurchase(ProductOnSale productOnSale, Integer quantity, User client, DeliveryMethod deliveryMethod, PaymentMethod paymentMethod, String address, Float coordX, Float coordY, Date dateOfPurchase) throws MLException {
        var weight = productOnSale.getProduct().getWeigth()*quantity;
        var p = new Purchase(productOnSale, quantity, client, deliveryMethod, paymentMethod, address, coordX, coordY, dateOfPurchase);
        if(deliveryMethod.getStartWeight() <= weight && deliveryMethod.getEndWeight() > weight ) {
            p =  purchaseRepository.save(p);
        }
        else {throw new MLException("método de delivery no válido");}
        return purchaseRepository.findById(p.getId()).get();
    }

    @Override @Transactional
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override @Transactional
    public Optional<Provider> getProviderByCuit(long cuit) {
        return providerRepository.findByCuit(cuit);
    }

    @Override @Transactional
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override @Transactional
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override @Transactional
    public ProductOnSale getProductOnSaleById(Long id) {
        return productOnSaleRepository.findById(id).get();
    }

    @Override @Transactional
    public Optional<DeliveryMethod> getDeliveryMethodByName(String name) {
        return deliveryMethodRepository.findByName(name);
    }

    @Override @Transactional
    public Optional<CreditCardPayment> getCreditCardPaymentByName(String name) {
        return creditCardPaymentRepository.findByName(name);
    }

    @Override @Transactional
    public Optional<OnDeliveryPayment> getOnDeliveryPaymentByName(String name) {
        return onDeliveryPaymentRepository.findByName(name);
    }

    @Override @Transactional
    public Optional<Purchase> getPurchaseById(Long id) {
        return purchaseRepository.findById(id);
    }

    @Override @Transactional
    public List<Purchase> getAllPurchasesMadeByUser(String userMail) {
        return  purchaseRepository.findByClientEmail(userMail);
    }

    @Override
    public List<User> getUsersSpendingMoreThanInPurchase(Float amount) {
        return purchaseRepository.findBySpendMoreThan(amount);
    }

    @Override
    public List<User> getUsersSpendingMoreThan(Float amount) {
        return purchaseRepository.getUsersSpendingMoreThan((double)amount);
    }

    @Override
    public List<Provider> getTopNProvidersInPurchases(int n) {
        var jpaSort=JpaSort.unsafe(Sort.Direction.DESC,"sum(pur.quantity)");
        return purchaseRepository.getTopNProvidersInPurchases(PageRequest.of(0,n, jpaSort));
    }

    @Override
    public List<Product> getTop3MoreExpensiveProducts() {
        return productOnSaleRepository.getMoreExpensiveProducts(PageRequest.of(0,3, Sort.Direction.DESC,"price"));
    }

    @Override
    public List<User> getTopNUsersMorePurchase(int n) {
        var jpaSort=JpaSort.unsafe(Sort.Direction.DESC,"count(pur.client)");
        return userRepository.getTopNUsersMorePurchase(PageRequest.of(0,n,jpaSort));
    }

    @Override @Transactional
    public List<Purchase> getPurchasesInPeriod(Date startDate, Date endDate) {
        return purchaseRepository.findByDateOfPurchaseBetween(startDate,endDate);
    }

    @Override
    public List<Product> getProductForCategory(Category category) {
        return productRepository.getByCategory(category);
    }

    @Override
    public List<Purchase> getPurchasesForProvider(Long cuit) {
        return purchaseRepository.getByProductOnSaleProviderCuit(cuit);
    }

    @Override
    public Product getBestSellingProduct() {
        var jpaSort=JpaSort.unsafe(Sort.Direction.DESC,"count(pur)");
        return purchaseRepository.getBestSellingProduct(PageRequest.of(0,1,jpaSort)).get(0);
    }

    @Override
    public List<Product> getProductsOnePrice() {
        return productOnSaleRepository.getProductsOnePrice();
    }

    @Override
    public List<Product> getProductWithMoreThan20percentDiferenceInPrice() {
        return mlRepository.getProductWithMoreThan20percentDiferenceInPrice();
    }

    @Override
    public Provider getProviderLessExpensiveProduct() {
        return productOnSaleRepository.getTopByOrderByPrice().get(0).getProvider();
    }

    @Override
    public List<Provider> getProvidersDoNotSellOn(Date day) {
        List<Provider> providers = purchaseRepository.findByDateOfPurchase(day).stream().map(purchase -> {
            return purchase.getProductOnSale().getProvider();
        }).collect(Collectors.toList());
        var productOnSales=productOnSaleRepository.findByProviderNotIn(providers);
        return productOnSales.stream().map(ProductOnSale::getProvider).distinct().collect(Collectors.toList());
    }

    @Override
    public List<ProductOnSale> getSoldProductsOn(Date day) {
        return purchaseRepository.getSoldProductsOn(day);
    }

    @Override
    public List<Product> getProductsNotSold() {
        return productRepository.getProductsNotSold();
    }

    @Override
    public DeliveryMethod getMostUsedDeliveryMethod() {
        return purchaseRepository.getMostUsedDeliveryMethod().get(0);
    }

    @Override
    public OnDeliveryPayment getMoreChangeOnDeliveryMethod() {
        return purchaseRepository.getMoreChangeOnDeliveryMethod().get(0);
    }

    @Override
    public Product getHeaviestProduct() {
        return productRepository.getHeaviestProduct().get(0);
    }

    @Override
    public Category getCategoryWithLessProducts() {
        return productRepository.getCategoryWithLessProducts().get(0);
    }
}
