package dev.lesechko.proselyte.service;

import dev.lesechko.proselyte.model.User;
import dev.lesechko.proselyte.repository.UserRepository;
import dev.lesechko.proselyte.repository.hibernate.HibernateUserRepositoryImpl;

import java.util.List;

public class UserService {
    private UserRepository userRepository = new HibernateUserRepositoryImpl();

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User getById(Integer id) {
        return userRepository.getById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean deleteById(Integer id) {
        return userRepository.deleteById(id);
    }

    public User update(User user) {
        return userRepository.update(user);
    }
}
