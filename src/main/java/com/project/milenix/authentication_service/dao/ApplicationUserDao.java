package com.project.milenix.authentication_service.dao;

import com.project.milenix.user_service.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationUserDao extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
