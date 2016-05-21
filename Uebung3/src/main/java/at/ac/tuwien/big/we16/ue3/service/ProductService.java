package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;

public class ProductService {

    private static final String PERSISTENCE_UNIT_NAME = "defaultPersistenceUnit";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private EntityManager em;

    public Collection<Product> getAllProducts() {
        em = factory.createEntityManager();
        Query query = em.createQuery("SELECT p FROM Product p");
        return (Collection<Product>) query.getResultList();
    }

    public Product getProductById(String id) throws ProductNotFoundException {
        em = factory.createEntityManager();
        return em.find(Product.class, id);
    }

    public Collection<Product> checkProductsForExpiration() {
        em = factory.createEntityManager();
        em.getTransaction().begin();

        Collection<Product> newlyExpiredProducts = new ArrayList<>();
        for (Product product : this.getAllProducts()) {
            if (!product.hasExpired() && product.hasAuctionEnded()) {
                product.setExpired();
                newlyExpiredProducts.add(product);
                if (product.hasBids()) {
                    Bid highestBid = product.getHighestBid();
                    for (User user : product.getUsers()) {
                        user.decrementRunningAuctions();
                        if (highestBid.isBy(user)) {
                            user.incrementWonAuctionsCount();
                        }
                        else {
                            user.incrementLostAuctionsCount();
                        }
                        em.merge(user);
                    }
                }

                em.merge(product);
            }
        }

        em.getTransaction().commit();
        em.close();

        return newlyExpiredProducts;
    }
}
