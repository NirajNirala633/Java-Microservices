package com.user.service.UserService.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.service.UserService.UserService;
import com.user.service.UserService.entities.User;
import com.user.service.UserService.exceptions.ResourceNotFoundException;
import com.user.service.UserService.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        // Save the user to the database and return the saved entity
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        // Retrieve all users from the database
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server !! : "+userId));
    }

    @Override
    public User updateUser(String userId, User user) {
        // Find the existing user by ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Get the existing user
        User existingUser = userOptional.get();
        
        // Update fields (assuming User has setters)
        // existingUser.setName(user.getName()); // Adjust based on your User entity fields
        // existingUser.setEmail(user.getEmail()); // Example field
        // existingUser.setAbout(user.getAbout()); // Example field
        
        // Save and return the updated user
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(String userId) {
        // Check if user exists before deleting
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        // Delete the user
        userRepository.deleteById(userId);
    }
}