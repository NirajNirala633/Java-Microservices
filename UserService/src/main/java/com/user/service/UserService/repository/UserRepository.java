package com.user.service.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.service.UserService.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
    
}
