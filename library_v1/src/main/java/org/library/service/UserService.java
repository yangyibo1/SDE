package org.library.service;

import java.util.List;
import org.library.model.Author;
import org.library.model.User;
import org.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository authorRepository) {
        this.repo = authorRepository;
    }

    /**
     * get user by name
     * @param username
     * @return author
     */
    public User getUserByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }

    /**
     * get user by id
     * @param id
     * @return
     */
    public User getUser(Integer id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * get user by username
     * @param name
     * @return
     */
    public User getUserByName(String name) {
        if (name == null) {
            return null;
        }

        return repo.findByUsername(name).orElse(null);
    }

    /**
     * add user
     * @param user
     * @return
     */
    public User addUser(User user) {
        return repo.save(user);
    }

    /**
     * get all users
     */
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    /**
     * delete user
     * @param id
     * @return
     */
    public boolean deleteUser(int id) {
        repo.deleteById(id);

        return true;
    }

    /**
     * only for testing
     */
    public void clearAll() {
        repo.deleteAll();
    }
}
