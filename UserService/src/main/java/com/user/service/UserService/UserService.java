package com.user.service.UserService;

import java.util.List;
import com.user.service.UserService.entities.User;

public interface UserService {
    
    // user operations

    // create
    User saveUser(User user);

    // get all user
    List<User> getAllUser();

    // get single user of given userId
    User getUser(String userId);

    // update the user with the given userId and updated user details
    User updateUser(String userId, User user);

    // delete the user with the given userId
    void deleteUser(String userId);
}