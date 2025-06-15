package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntityExposureService {
    private final UserRepository userRepository;
    
    // Constructor injection is preferred over field injection
    public EntityExposureService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // BAD: Directly exposing entities in API responses
    public List<User> getAllUsersBadPractice() {
        // This exposes all fields from the entity, including relationships
        // Can lead to serialization issues and exposing internal data model
        return userRepository.findAllWithOrders();
    }
    
    // GOOD: Using DTOs to control what data is exposed
    public List<UserDTO> getAllUsersGoodPractice() {
        // Using DTOs to map only the data we want to expose
        List<User> users = userRepository.findAllWithOrders();
        return UserDTO.fromEntities(users);
    }
}
