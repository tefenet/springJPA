package com.protonmail.slobodo.bd2.services;

import com.protonmail.slobodo.bd2.repositories.MLException;
import com.protonmail.slobodo.bd2.elastic_repositories.*;
import com.protonmail.slobodo.bd2.model.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ElasticMLService implements MLService{

    @Autowired
    private CategoryESRepository categoryRepository;
    @Autowired
    private ProductESRepository productRepository;
    @Autowired
    private ProviderESRepository providerRepository;
    @Autowired
    private ProductOnSaleESRepository productOnSaleRepository;
    @Autowired
    private CreditCardPaymentESRepository creditCardPaymentRepository;
    @Autowired
    private DeliveryMethodESRepository deliveryMethodRepository;
    @Autowired
    private OnDeliveryPaymentESRepository onDeliveryPaymentRepository;
    @Autowired
    private PurchaseESRepository purchaseRepository;
    @Autowired
    private UserESRepository userRepository;

    private <T extends MlBdd2AbstractEntity> MlBdd2AbstractEntity create(ElasticsearchRepository<T,Long> repository, T o){
        try {
            return repository.save(o);
        }catch (DataIntegrityViolationException e){
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t != null) {
                throw new MLException("Constraint Violation");
            }
        }
        return null;
        /*var optionalT= repository.findById(o.getId());
        if (optionalT.isPresent()) {
            return optionalT.get();
        } else {
            throw new MLException(String.format("%s not found", o.getClass().getSimpleName()));
        }*/
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
        return null;
    }

    @Override
    public DeliveryMethod createDeliveryMethod(String name, Float cost, Float startWeight, Float endWeight) throws MLException {
        return null;
    }

    @Override
    public CreditCardPayment createCreditCardPayment(String name, String brand, Long number, Date expiry, Integer cvv, String owner) throws MLException {
        return null;
    }

    @Override
    public OnDeliveryPayment createOnDeliveryPayment(String name, Float promisedAmount) throws MLException {
        return null;
    }

    @Override
    public ProductOnSale createProductOnSale(Product product, Provider provider, Float price, Date initialDate) throws MLException {
        return null;
    }

    @Override
    public Purchase createPurchase(ProductOnSale productOnSale, Integer quantity, User client, DeliveryMethod deliveryMethod, PaymentMethod paymentMethod, String address, Float coordX, Float coordY, Date dateOfPurchase) throws MLException {
        return null;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Provider> getProviderByCuit(long cuit) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Optional<Product> getProductByName(String name) {
        return Optional.empty();
    }

    @Override
    public ProductOnSale getProductOnSaleById(Long id) {
        return null;
    }

    @Override
    public Optional<DeliveryMethod> getDeliveryMethodByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<CreditCardPayment> getCreditCardPaymentByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<OnDeliveryPayment> getOnDeliveryPaymentByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Purchase> getPurchaseById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Purchase> getAllPurchasesMadeByUser(String username) {
        return null;
    }

    @Override
    public List<User> getUsersSpendingMoreThanInPurchase(Float amount) {
        return null;
    }

    @Override
    public List<User> getUsersSpendingMoreThan(Float amount) {
        return null;
    }

    @Override
    public List<Provider> getTopNProvidersInPurchases(int n) {
        return null;
    }

    @Override
    public List<Product> getTop3MoreExpensiveProducts() {
        return null;
    }

    @Override
    public List<User> getTopNUsersMorePurchase(int n) {
        return null;
    }

    @Override
    public List<Purchase> getPurchasesInPeriod(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public List<Product> getProductForCategory(Category category) {
        return null;
    }

    @Override
    public List<Purchase> getPurchasesForProvider(Long cuit) {
        return null;
    }

    @Override
    public Product getBestSellingProduct() {
        return null;
    }

    @Override
    public List<Product> getProductsOnePrice() {
        return null;
    }

    @Override
    public List<Product> getProductWithMoreThan20percentDiferenceInPrice() {
        return null;
    }

    @Override
    public Provider getProviderLessExpensiveProduct() {
        return null;
    }

    @Override
    public List<Provider> getProvidersDoNotSellOn(Date day) {
        return null;
    }

    @Override
    public List<ProductOnSale> getSoldProductsOn(Date day) {
        return null;
    }

    @Override
    public List<Product> getProductsNotSold() {
        return null;
    }

    @Override
    public DeliveryMethod getMostUsedDeliveryMethod() {
        return null;
    }

    @Override
    public OnDeliveryPayment getMoreChangeOnDeliveryMethod() {
        return null;
    }

    @Override
    public Product getHeaviestProduct() {
        return null;
    }

    @Override
    public Category getCategoryWithLessProducts() {
        return null;
    }
}
