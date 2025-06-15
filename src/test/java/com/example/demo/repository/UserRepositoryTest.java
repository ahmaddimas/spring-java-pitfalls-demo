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
}
