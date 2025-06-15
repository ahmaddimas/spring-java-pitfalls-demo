package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@SpringBootTest
class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(userRepository, orderRepository);
        
        // Setup mock repository
        User mockUser = new User();
        ReflectionTestUtils.setField(mockUser, "id", 1L);
        mockUser.setName("Test User");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            if (savedUser.getId() == null) {
                ReflectionTestUtils.setField(savedUser, "id", 99L);
            }
            return savedUser;
        });
        
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            if (savedOrder.getId() == null) {
                ReflectionTestUtils.setField(savedOrder, "id", 999L);
            }
            return savedOrder;
        });
    }

    @Test
    void testGetUserByIdBadPractice() {
        // Both methods are functionally the same, but the bad practice
        // uses @Transactional which is unnecessary for read operations
        User user = transactionService.getUserByIdBadPractice(1L);
        
        assertNotNull(user);
        assertEquals(1L, user.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserByIdGoodPractice() {
        // Good practice uses @Transactional(readOnly = true) for optimization
        User user = transactionService.getUserByIdGoodPractice(1L);
        
        assertNotNull(user);
        assertEquals(1L, user.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void testCreateUserWithOrdersBadPractice() {
        // This test demonstrates that no transaction would result in
        // partial updates if the second operation fails
        
        // Mock orderRepository to throw exception
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Database error"));
        
        // Execute and verify
        assertThrows(RuntimeException.class, 
            () -> transactionService.createUserWithOrdersBadPractice("New User", "New Product"));
        
        // Verify that userRepository.save was called - which would commit in a real scenario
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUserWithOrdersGoodPractice() {
        // This test demonstrates that with proper transaction management,
        // all operations are rolled back if any fails
        
        // Mock orderRepository to throw exception
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Database error"));
        
        // Execute and verify
        assertThrows(RuntimeException.class, 
            () -> transactionService.createUserWithOrdersGoodPractice("New User", "New Product"));
        
        // In a real Spring application, the transaction would be rolled back
        // and the user would not be saved
        
        // Note: We can't completely verify transaction behavior in a unit test
        // since @Transactional is implemented through AOP proxies
        // A proper integration test would be needed
    }
}
