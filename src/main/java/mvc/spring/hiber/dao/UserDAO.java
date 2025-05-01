package mvc.spring.hiber.dao;

import java.util.List;

import mvc.spring.hiber.model.User;

public interface UserDAO {
    public List<User> allUsers();

    public User userById(int id);

    public void save(User user);

    public void update(int id, User updatedUser);

    public void delete(int id);
}