package com.espeditomelo.myblog.service.serviceImpl;

import com.espeditomelo.myblog.model.User;
import com.espeditomelo.myblog.model.repository.UserRepository;
import com.espeditomelo.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;



    @Override
    public List<User> findAllEnabled() {
        return userRepository.findAllEnabled();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User save(User user) {
        if(user.getPassword() != null){
            //user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void logicalDeleteUser(Long id) {
       userRepository.logicalDeleteUser(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
