package com.rpissarra.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public Optional<User> findByUserId(UUID userId) {
        return findAllUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();
    }

    public List<User> findAllUsers() {
        return userDao.findAll();
    }
}
