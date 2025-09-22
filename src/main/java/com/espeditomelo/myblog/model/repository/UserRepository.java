package com.espeditomelo.myblog.model.repository;

import com.espeditomelo.myblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.enable = false WHERE u.id = :id")
    void logicalDeleteUser(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.enable = true")
    List<User> findAllEnabled();
}
