package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class ConstructorInjectionService {
    
    // GOOD: Constructor injection
    // Makes dependencies explicit, allows final fields, easier to test
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    
    // Constructor is automatically autowired by Spring
    public ConstructorInjectionService(UserRepository userRepository, 
                                      OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }
    
    public String getRepositoryStatusGoodPractice() {
        return "Using UserRepository: " + (userRepository != null) +
               " and OrderRepository: " + (orderRepository != null);
    }
}
