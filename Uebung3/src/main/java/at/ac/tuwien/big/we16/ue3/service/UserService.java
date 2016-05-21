package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.exception.ValidationException;
import at.ac.tuwien.big.we16.ue3.model.User;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public class UserService {

    private static final String PERSISTENCE_UNIT_NAME = "defaultPersistenceUnit";
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private  EntityManager em;

    public void createUser(User user) throws ValidationException {

        ValidatorFactory validationFactory = Validation.buildDefaultValidatorFactory();

        Validator validator = validationFactory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(user);

//        if (!user.overEighteen()) {
//            ConstraintViolation<User> violation =
//                    ConstraintViolationImpl.forBeanValidation("","you must be over 18", User.class, user, user, user.getDate(), PathImpl.createPathFromString("date"), null, null);
//            constraintViolations.add((ConstraintViolation) violation);
//        }

        if (constraintViolations.size() > 0) {
            throw new ValidationException(constraintViolations);
        }

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
