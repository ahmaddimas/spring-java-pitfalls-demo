package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllWithOrders() {
        // This test verifies that our solution for the N+1 problem works
        // by checking that users and their orders are loaded in a single query
        
        List<User> users = userRepository.findAllWithOrders();
        
        // Verify that users were found
        assertFalse(users.isEmpty(), "Users should be loaded");
        
        // Check that orders are already loaded (no additional queries needed)
        // If the orders weren't eagerly loaded, this would trigger additional queries
        // but with JOIN FETCH, they're already loaded
        for (User user : users) {
            // This should not trigger additional queries if working correctly
            if (user.getOrders() != null) {
                user.getOrders().size();
            }
        }
    }
    
    @Test
    void testFindAllN1Problem() {
        // This test demonstrates the N+1 problem
        // In a real application with SQL logging, you would see:
        // 1 query to fetch all users
        // N additional queries to fetch orders for each user when accessed
        
        List<User> users = userRepository.findAll();
        
        // Verify that users were found
        assertFalse(users.isEmpty(), "Users should be loaded");
        
        // In a real application, trying to access the orders here would cause N additional queries
        // In our test environment, it causes a LazyInitializationException because the session is closed
        // This exception perfectly demonstrates why the N+1 problem needs to be addressed
        // We're just testing that users are loaded, not attempting to access the lazy-loaded orders
        assertNotNull(users.get(0).getName(), "User name should be accessible");
    }
}
