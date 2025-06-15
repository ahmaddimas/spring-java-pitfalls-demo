package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ExceptionHandlingService.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@SpringBootTest
class ExceptionHandlingServiceTest {

    @Mock
    private UserRepository userRepository;

    private ExceptionHandlingService exceptionHandlingService;

    @BeforeEach
    void setUp() {
        exceptionHandlingService = new ExceptionHandlingService(userRepository);
        
        // Setup mock repository
        User mockUser = new User();
        org.springframework.test.util.ReflectionTestUtils.setField(mockUser, "id", 1L);
        mockUser.setName("Test User");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
    }

    @Test
    void testGetUserByIdBadPractice_WhenUserExists() {
        User user = exceptionHandlingService.getUserByIdBadPractice(1L);
        
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
    }

    @Test
    void testGetUserByIdBadPractice_WhenUserDoesNotExist() {
        // With bad practice, the method swallows exceptions and returns null
        User user = exceptionHandlingService.getUserByIdBadPractice(2L);
        
        assertNull(user, "Bad practice: Swallows exception and returns null");
    }

    @Test
    void testGetUserByIdGoodPractice_WhenUserExists() {
        User user = exceptionHandlingService.getUserByIdGoodPractice(1L);
        
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
    }

    @Test
    void testGetUserByIdGoodPractice_WhenUserDoesNotExist() {
        // Good practice throws a specific exception that can be handled globally
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> exceptionHandlingService.getUserByIdGoodPractice(2L)
        );
        
        assertTrue(exception.getMessage().contains("User not found with id: 2"));
    }
}
