package com.espeditomelo.myblog.service;

import com.espeditomelo.myblog.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAllEnabled();
    User findById(Long id);
    User save(User user);
    void logicalDeleteUser(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
