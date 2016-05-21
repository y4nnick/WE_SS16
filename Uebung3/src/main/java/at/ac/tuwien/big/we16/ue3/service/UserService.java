package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserService {

    private static final String PERSISTENCE_UNIT_NAME = "defaultPersistenceUnit";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private  EntityManager em;

    public void createUser(User user) {

        em = factory.createEntityManager();
        em.getTransaction().begin();

        em.persist(user);

        em.getTransaction().commit();
        em.close();

    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        em = factory.createEntityManager();

        TypedQuery<User> userTypedQuery = em.createQuery("SELECT u FROM User u WHERE u.email = :useremail", User.class);
        userTypedQuery.setParameter("useremail", email);

        List<User> result =  userTypedQuery.getResultList();

        if(result == null || result.size() == 0){
            throw new UserNotFoundException();
        }

        em.close();

        return result.get(0);
    }
}
