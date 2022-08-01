package com.protonmail.slobodo.bd2.repositories;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import com.protonmail.slobodo.bd2.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;



public class MLRepository {
	@Autowired
	private SessionFactory sessionFactory;
	private final QueryMaker QUERY_MAKER = new QueryMaker();
	private static final String INNER_JOIN_O_PRODUCT_ON_SALE_P = "inner join o.productOnSale p ";

	public Serializable save(Object object) throws MLException{
		return QUERY_MAKER.save(object);
	}
	public Optional<Provider> getProviderByCuit(Long cuit) {
		return QUERY_MAKER.findOneByQuery("from Provider where cuit = :cuit", Map.of("cuit",cuit), Provider.class);
	}

	public Optional<User> getUserByEmail(String email) {
		return QUERY_MAKER.findOneByQuery("from User where email = :email", Map.of("email",email), User.class);
	}

	public Optional<Product> getProductByName(String name) {
		return QUERY_MAKER.findByName(name, Product.class);
	}

	public Optional<Category> getCategoryByName(String name) {
		return QUERY_MAKER.findByName(name, Category.class);
	}

	public Optional<DeliveryMethod> getDeliveryMethodByName(String name) {
		return QUERY_MAKER.findByName(name, DeliveryMethod.class);
	}

	public Optional<CreditCardPayment> getCreditCardPaymentByName(String name) {
		return QUERY_MAKER.findByName(name, CreditCardPayment.class);
	}

	public Optional<OnDeliveryPayment> getOnDeliveryPaymentByName(String name) {
		return QUERY_MAKER.findByName(name, OnDeliveryPayment.class);
	}

	public Optional<Purchase> getPurchaseById(Long id) {
		var hql = "from Purchase where id = :id";
		return QUERY_MAKER.findOneByQuery(hql, Map.of("id",id), Purchase.class);
	}

	public List<ProductOnSale> findProductOnSale(ProductOnSale pOnSale) {
		var hql = "from ProductOnSale where product_id = :product_id and provider_id = :provider_id";
		Map<String,?> params =Map.of("product_id",pOnSale.getProduct().getId(), "provider_id", pOnSale.getProvider().getId());
		return QUERY_MAKER.findManyByQuery(hql, params, ProductOnSale.class);
	}

	public List<Purchase> getAllPurchasesMadeByUser(String email){
		return QUERY_MAKER.findManyByQuery("select o from Purchase o inner join o.client u where u.email = :email",Map.of("email",email),Purchase.class);
	}

	public List<User> getUsersSpendingMoreThanInPurchase(Float amount){
		return QUERY_MAKER.findManyByQuery("select c from Purchase p inner join p.client c inner join p.productOnSale pos inner join pos.product prod where (pos.price * p.quantity) > :amount",Map.of("amount",amount),User.class);
	}
	// Fixed! El test ANDA MAL. MySQL DEBUG QUERY: "select * from ProductOnSale as pos inner join Product p on pos.product_id = p.id group by p.id order by pos.price desc;"
	public List<Product> getTop3MoreExpensiveProducts(){
		return QUERY_MAKER.findManyByQueryLimit("select p from ProductOnSale pos inner join pos.product p "
				+ "group by pos order by pos.price desc",new HashMap<>(),Product.class,3);
	}
	@Transactional
	public List<User> getTopNUsersMorePurchase(int n){
		return QUERY_MAKER.findManyByQueryLimit("select u from Purchase pur inner join pur.client u group by u "
				+ "order by count(u.id) desc", new HashMap<>(),User.class,n);
	}
	public List<Purchase> getPurchasesInPeriod(Date startDate, Date endDate) {
		return QUERY_MAKER.findManyByQuery("from Purchase where dateOfPurchase between :start and :end", Map.of("start", startDate, "end", endDate)
				, Purchase.class);
	}

	@Transactional
	public List<Purchase> getPurchasesByProvider(Long cuit) {
		return QUERY_MAKER.findManyByQuery("select o from Purchase o inner join o.productOnSale pos inner join pos.provider prov where prov.cuit = :cuit",Map.of("cuit",cuit),Purchase.class);
	}

	public Product getBestSellingProduct() {
		var hql = "select p from Purchase pur inner join pur.productOnSale pos inner join pos.product p group by p.id order by count(pur.id) desc";
		return QUERY_MAKER.findOneByQueryLimit(hql,new HashMap<>(),Product.class,1).get();
	}

	public List<Product> getProductsOnePrice() {
		return QUERY_MAKER.findManyByQuery("select p from Product p inner join p.productsOnSale pos group by p.id having count(pos.id)=1",Map.of(), Product.class);
	 }
	// TODO: esto NO anda el test espera 29 productos, y esto devuelve SIEMPRE 42 en cualquiera de las 4 queries estas:
	public List<Product> getProductWithMoreThan20percentDiferenceInPrice(){
		//return QUERY_MAKER.findManyByQuery("select distinct pos.product FROM ProductOnSale pos inner join pos.product prod WHERE 0.2 < (select (max(p.price) / min(p.price)) -1 From ProductOnSale p inner join p.product group by prod.id)", new HashMap<String, Object>(),Product.class);
		//return QUERY_MAKER.findManyByQuery("select prod FROM ProductOnSale pos inner join pos.product prod WHERE pos.finalDate = null GROUP BY prod HAVING  ( (max(pos.price) - min(pos.price))/max(pos.price) > 0.20 )", new HashMap<String, Object>(),Product.class);
		//return QUERY_MAKER.findManyByQuery("select p FROM Product p inner join p.productsOnSale pos WHERE pos.finalDate is null GROUP BY p HAVING  ( (max(pos.price) - min(pos.price))/max(pos.price) > 0.2 )", Map.of(),Product.class);
		return QUERY_MAKER.findManyByQuery("select pos.product FROM ProductOnSale pos WHERE pos.finalDate is null GROUP BY pos.product HAVING  ( (max(pos.price) - min(pos.price))/max(pos.price) > 0.2 )", Map.of(),Product.class);

	}

	public Provider getProviderLessExpensiveProduct() {
		var hql = "select prov from Product p inner join p.productsOnSale pos inner join pos.provider prov order by pos.price asc";
		return QUERY_MAKER.findOneByQueryLimit(hql,new HashMap<>(),Provider.class,1).get();
	}

	public List<Product> getProductsNotSold() {
		return QUERY_MAKER.findManyByQuery("select pr from Product pr where pr.id not in"
					+" (select distinct prod.id from Purchase pur inner join pur.productOnSale pos"
					+ " inner join pos.product prod )",new HashMap<>(),Product.class);
	}
	public List<Provider> getTopNProvidersInPurchases(int n) {
		return QUERY_MAKER.findManyByQueryLimit("select prov from Purchase p "
	        		+ "inner join p.productOnSale pos "
	        		//+ "inner join pos.product p1 "
	        		+ "inner join pos.provider prov "
	        		+ "group by prov "
	        		+ "order by sum(p.quantity) desc",new HashMap<>(),Provider.class,n);
	}
	// 3 - sum() de HQL devuelve un  Double, por eso el Cast
	public List<User> getUsersSpendingMoreThan(Float amount) {
		var hql = "select c from Purchase p inner join p.client c inner join p.productOnSale pos inner join pos.product prod " +
				"group by c " +
				"having (sum(pos.price * p.quantity) > :amount )";
		return QUERY_MAKER.findManyByQuery(hql,Map.of("amount",amount.doubleValue()),User.class);
	}
	public List<ProductOnSale> getSoldProductsOn(Date day) {
		var hql= "select distinct pos from Purchase pur inner join pur.productOnSale pos "
				+ "where pur.dateOfPurchase = :day ";
		return QUERY_MAKER.findManyByQuery(hql ,Map.of("day", day),ProductOnSale.class);
	}
	public Product getHeaviestProduct() {
		var hql="select p from Product p ORDER BY p.weigth DESC";
		return QUERY_MAKER.findOneByQueryLimit(hql, new HashMap<>(), Product.class,1).get();
	}

	public Category getCategoryWithLessProducts() {
		return QUERY_MAKER.findManyByQueryLimit("select c from Product p inner join p.category c group by c order by count(p) asc",new HashMap<>(),Category.class,1).get(0);
	}

	public List<Product> getProductForCategory(Category category) {
		return QUERY_MAKER.findManyByQuery("from Product where category = :category", Map.of("category",category),Product.class);
	}

	public List<Provider> getProvidersDoNotSellOn(Date day) {
		var hql = "SELECT prov from Provider prov where prov.id NOT IN "
				+ "(SELECT prov.id FROM Purchase purchase inner join purchase.productOnSale pos inner join pos.provider prov WHERE purchase.dateOfPurchase = :day)";
		return QUERY_MAKER.findManyByQuery(hql,Map.of("day", day),Provider.class);
	}

	public DeliveryMethod getMostUsedDeliveryMethod() {
		var hql="SELECT dm from Purchase pur inner join pur.deliveryMethod dm group by dm.id ORDER BY COUNT(*) DESC";
		return QUERY_MAKER.findOneByQueryLimit(hql,new HashMap<>(),DeliveryMethod.class,1).get();
	}

	public OnDeliveryPayment getMoreChangeOnDeliveryMethod() {
		var hql="SELECT pm from Purchase pur inner join pur.paymentMethod pm where pm.class = OnDeliveryPayment order by (pm.promisedAmount - pur.amount) DESC";
		return (OnDeliveryPayment) QUERY_MAKER.findOneByQueryLimit(hql,new HashMap<>(),PaymentMethod.class,1).get();
	}

	public Category findCategoryById(Long cat) {
		return QUERY_MAKER.findById(cat,Category.class);
	}

	public Product findProductById(Long id) {
		return QUERY_MAKER.findById(id,Product.class);
	}

	public User findUserById(Long id) {
		return QUERY_MAKER.findById(id, User.class);
	}

	public Provider findProviderById(Long id) {
		return QUERY_MAKER.findById(id, Provider.class);
	}

	public DeliveryMethod findDeliveryMethodById(Long dmId) {
		return QUERY_MAKER.findById(dmId , DeliveryMethod.class);
	}

	public CreditCardPayment findCreditCardPaymentById(Long ccPayId) {
		return QUERY_MAKER.findById(ccPayId,CreditCardPayment.class);
	}

	public OnDeliveryPayment findOnDeliveryPaymentById(Long onDPId) {
		return QUERY_MAKER.findById(onDPId, OnDeliveryPayment.class);
	}

	public ProductOnSale findProductOnSaleById(Long pOnSaleId) {
		return QUERY_MAKER.findById(pOnSaleId, ProductOnSale.class);
	}

	public Purchase findPurchaseById(long pId) {
		return QUERY_MAKER.findById(pId,Purchase.class);
	}

	private class QueryMaker {

		private final Logger LOGGER = Logger.getLogger(QueryMaker.class.getName());
		public Serializable save(Object object) throws MLException{
			var session = MLRepository.this.sessionFactory.openSession();
			Serializable saved = null;
			try {
				session.beginTransaction();
				saved = session.save(object);
				session.getTransaction().commit();
			}
			catch (PersistenceException e) {
				LOGGER.severe(e.getMessage());
				Throwable t = e.getCause();
				while ((t != null) && !(t instanceof ConstraintViolationException)) {
					t = t.getCause();
				}
				if (t != null) {
					throw new MLException("Constraint Violation");
				}
			}
			catch(Exception e){
				LOGGER.severe("Generic Exception " + e.getMessage());
			}
			finally {
				session.close();
			}
			return saved;
		}
		public <T> Object find(String hql, Map<String,?> params, Method method, Class<T> aClass) {
			var session=MLRepository.this.sessionFactory.openSession();
			Object entity= null;
			try {
				session.beginTransaction();
				Query<T> query = session.createQuery(hql, aClass);
				params.forEach(query::setParameter);
				entity = method.invoke(query);
				session.getTransaction().commit();
			} catch (Exception e) {
				LOGGER.severe(e.getMessage());
			}
			finally {
				session.close();
			}
			return entity;
		}

		public <T> Object findMax(String hql, Map<String,?> params, Method method, Class<T> aClass, int n) {
			var session=MLRepository.this.sessionFactory.openSession();
			Object entity= null;
			try {
				session.beginTransaction();
				Query<T> query = session.createQuery(hql, aClass);
				params.forEach(query::setParameter);
				entity = method.invoke(query.setMaxResults(n));
				session.getTransaction().commit();
			} catch (Exception e) {
				LOGGER.severe(e.getMessage());
			}
			finally {
				session.close();
			}
			return entity;
		}

		public <T> List<T> findManyByQuery(String hql, Map<String,?> params, Class<T> aClass) {
			Method method= null;
			try {
				method = Query.class.getMethod("getResultList");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return (List<T>) find(hql,params,method,aClass);
		}

		public <T> Optional<T> findOneByQuery(String hql, Map<String,?> params, Class<T> aClass) {
			Method method= null;
			try {
				method = Query.class.getMethod("uniqueResultOptional");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return (Optional<T>) find(hql,params,method,aClass);
		}

		public <T> Optional<T> findOneByQueryLimit(String hql, Map<String,?> params, Class<T> aClass,int n) {
			Method method= null;
			try {
				method = Query.class.getMethod("uniqueResultOptional");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return (Optional<T>) findMax(hql,params,method,aClass,n);
		}

		public <T> List<T> findManyByQueryLimit(String hql, Map<String,?> params, Class<T> aClass, Integer n) {
			Method method= null;
			try {
				method = Query.class.getMethod("getResultList");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return (List<T>) findMax(hql,params,method,aClass,n);
		}

		public  <T> T findById(Long id, Class<T> aClass){
			return findOneByQuery("from "+ aClass.getSimpleName()+" where id = :id",Map.of("id",id), aClass).orElse(null);
		}
		public <T> Optional<T> findByName(String name, Class<T> aClass) {
			return findOneByQuery(String.format("from %s where name LIKE :name", aClass.getSimpleName()),Map.of("name","%"+name+"%"), aClass);
		}

	}

}
