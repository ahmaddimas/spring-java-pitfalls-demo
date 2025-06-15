package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.Order;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    
    public TransactionService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }
    
    // BAD: Using @Transactional for a simple read operation
    @Transactional
    public User getUserByIdBadPractice(Long id) {
        // Simple read operations don't need transactions
        return userRepository.findById(id).orElse(null);
    }
    
    // GOOD: Using readOnly=true for read operations
    @Transactional(readOnly = true)
    public User getUserByIdGoodPractice(Long id) {
        // readOnly=true gives database optimizations for read operations
        return userRepository.findById(id).orElse(null);
    }
    
    // BAD: No transaction for multiple write operations
    public void createUserWithOrdersBadPractice(String userName, String productName) {
        User user = new User();
        user.setName(userName);
        userRepository.save(user);
        
        // If this fails, the user is still saved - inconsistent state
        Order order = new Order();
        order.setProduct(productName);
        order.setUser(user);
        orderRepository.save(order);
    }
    
    // GOOD: Using @Transactional for multiple write operations
    @Transactional
    public void createUserWithOrdersGoodPractice(String userName, String productName) {
        User user = new User();
        user.setName(userName);
        userRepository.save(user);
        
        // If this fails, the transaction is rolled back - user is not saved
        Order order = new Order();
        order.setProduct(productName);
        order.setUser(user);
        orderRepository.save(order);
    }
    
    // BAD: Using separate transaction methods that should be in one transaction
    @Transactional
    public User createUserBadPractice(String userName) {
        User user = new User();
        user.setName(userName);
        return userRepository.save(user);
    }
    
    @Transactional
    public Order createOrderBadPractice(User user, String productName) {
        Order order = new Order();
        order.setProduct(productName);
        order.setUser(user);
        return orderRepository.save(order);
    }
    
    // Example of calling the bad practice methods
    public void processUserAndOrderBadPractice(String userName, String productName) {
        // These are in separate transactions - if second fails, first is still committed
        User user = createUserBadPractice(userName);
        createOrderBadPractice(user, productName); // If this fails, user is still saved
    }
}
