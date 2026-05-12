package com.rpissarra.user;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public Optional<User> findByUserId(UUID userId) {
        User[] users = findAllUsers();
        for (User u : users) {
            if (u.getId().equals(userId)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public User[] findAllUsers() {
        return userDao.findAll();
    }
}
