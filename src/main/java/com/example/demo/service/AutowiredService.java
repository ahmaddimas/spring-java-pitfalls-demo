package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutowiredService {
    
    // BAD: Field injection with @Autowired
    // Hard to test, hides dependencies, can't make fields final
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // No constructor needed with field injection
    
    public String getRepositoryStatusBadPractice() {
        return "Using UserRepository: " + (userRepository != null) +
               " and OrderRepository: " + (orderRepository != null);
    }
}
