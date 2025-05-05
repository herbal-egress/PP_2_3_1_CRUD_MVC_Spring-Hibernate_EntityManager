package mvc.spring.hiber.dao;

import mvc.spring.hiber.model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
@Transactional(readOnly = true)
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> allUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User userById(int id) {
        TypedQuery<User> tQuery = entityManager.createQuery("SELECT u FROM User u where u.id = :id", User.class);
        tQuery.setParameter("id", id);
        return tQuery.getResultList().stream().findAny().orElse(null);
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public void update(int id, User updatedUser) {
        User userTeBeUpdated = entityManager.find(User.class, id);
        userTeBeUpdated.setName(updatedUser.getName());
        userTeBeUpdated.setAge(updatedUser.getAge());
        userTeBeUpdated.setEmail(updatedUser.getEmail());
        entityManager.merge(userTeBeUpdated);
    }

    @Override
    @Transactional
    public void delete(int id) {
        User userTeBeDeleted = entityManager.find(User.class, id);
        entityManager.remove(userTeBeDeleted);
    }
}