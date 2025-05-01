package mvc.spring.hiber.dao;

import mvc.spring.hiber.model.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

// класс реализации всех методов приложения. Общается с БД, извлекает людей из списка, находит по id и т.д.
@Component
@Transactional(readOnly = true) // для автотранзакций Хайбернейта (тут применяется ко всем методам класса, у каждого
// метода тело будет объединено в одну транзакцию)
public class UserDAOImpl implements UserDAO {

    @PersistenceContext // инжектим EntityManager
    private EntityManager entityManager;

//    public UserDAOImpl(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }

    // метод только для чтения всех юзеров из БД.
    @Override
    public List<User> allUsers() {
        //Тупо пишем ниже HQL.  Внутри этого метода Спрингом автоматически откроется транзакция для Hibernate
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList(); // HQL-запрос.
        // Получаем List из людей.
    }

    // получаю юзера по id
    @Override
    public User userById(int id) {
//        User user=entityManager.find(User.class, id);  // можно и так искать по id
        TypedQuery<User> tQuery = entityManager.createQuery("SELECT u FROM User u where u.id = :id", User.class);
        tQuery.setParameter("id", id);
//        return tQuery.getSingleResult();
        return tQuery.getResultList().stream().findAny().orElse(null); // что бы не было ексепшена, если результат пустой - вернуть null
    }

    // создание нового юзера:
    @Override
    @Transactional // переопределение аннотации для метода, записывающего данные в БД
    public void save(User user) {
        entityManager.persist(user); // сохраняем человека, который пришёл с формы new.html
    }

    // обновление данных юзера
    @Override
    @Transactional // переопределение аннотации для метода, записывающего данные в БД
    public void update(int id, User updatedUser) {
        User userTeBeUpdated = entityManager.find(User.class, id);  // можно и так искать по id
//        TypedQuery<User> tQuery = entityManager.createQuery("SELECT u FROM User u where u.id = :id", User.class);
//        tQuery.setParameter("id", id);
//        User userTeBeUpdated = tQuery.getResultList().stream().findAny().orElse(null); // что бы не было ексепшена,
//        // если результат пустой - вернуть null
//        assert userTeBeUpdated != null; // проверяем, что юзер не null, можно не проверять, т.к. проверили строкой выше
        userTeBeUpdated.setName(updatedUser.getName()); // В хайбернейте userTeBeUpdated в состоянии Persistent/MANAGED,
        // поэтому вызов на нём сеттеров будет приводить к обновлению информации в БД у юзера с заданным id на то значение,
        // которое пришло с формы edit.html (через get-еры)
        userTeBeUpdated.setAge(updatedUser.getAge()); // если какое-то поле тут не укажем, то не сможем обновлять это значение!!!
        userTeBeUpdated.setEmail(updatedUser.getEmail());
        entityManager.merge(userTeBeUpdated);
    }
// удаление юзера по id
    @Override
    @Transactional // переопределение аннотации для метода, записывающего данные в БД
    public void delete(int id) {
        User userTeBeDeleted = entityManager.find(User.class, id); // можно и так искать по id
//        TypedQuery<User> tQuery = entityManager.createQuery("SELECT u FROM User u where u.id = :id", User.class);
//        tQuery.setParameter("id", id);
//        User userTeBeDeleted = tQuery.getResultList().stream().findAny().orElse(null); // что бы не было ексепшена,
//        // если результат пустой - вернуть null
        entityManager.remove(userTeBeDeleted); //  и сразу удаляем его из БД
    }
}