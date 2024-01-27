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
}
