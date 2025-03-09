package com.user.service.UserService.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.user.service.UserService.entities.Hotel;
import com.user.service.UserService.entities.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.user.service.UserService.UserService;
import com.user.service.UserService.entities.User;
import com.user.service.UserService.exceptions.ResourceNotFoundException;
import com.user.service.UserService.external.services.HotelService;
import com.user.service.UserService.repository.UserRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        User user =  userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server !! : "+userId));

        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);

        logger.info("{}",ratingsOfUser);

        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();


        List<Rating> ratingList = ratings.stream().map(rating -> {
//            api call to hotel service to get the hotel
//            http://localhost:8081/hotels/ccb3eea5-8307-4614-95dd-94e8a3a825a2
            // ResponseEntity<Hotel> forEntity =  restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            // Hotel hotel = forEntity.getBody();

            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            // logger.info("response status code: {}",forEntity.getStatusCode());

//            set the hotel to rating
            rating.setHotel(hotel);

//            return the rating
            return  rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
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