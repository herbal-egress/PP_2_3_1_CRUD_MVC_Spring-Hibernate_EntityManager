package mvc.spring.hiber.services;

import mvc.spring.hiber.dao.UserDAO;
import mvc.spring.hiber.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional(readOnly = true)
    public List<User> allUsers() {
        return userDAO.allUsers();
    }

    @Transactional(readOnly = true)
    public User userById(int id) {
        return userDAO.userById(id);
    }

    @Transactional
    public void save(User user) {
        userDAO.save(user);
    }

    @Transactional
    public void update(int id, User updatedUser) {
        userDAO.update(id, updatedUser);
    }

    @Transactional
    public void delete(int id) {
        userDAO.delete(id);
    }
}