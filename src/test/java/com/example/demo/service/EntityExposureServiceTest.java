package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EntityExposureServiceTest {

    @Mock
    private UserRepository userRepository;

    private EntityExposureService entityExposureService;
    
    private List<User> mockUsers;

    @BeforeEach
    void setUp() {
        entityExposureService = new EntityExposureService(userRepository);
        
        // Create test data
        mockUsers = new ArrayList<>();
        User user1 = new User();
        ReflectionTestUtils.setField(user1, "id", 1L);
        user1.setName("Test User 1");
        
        List<Order> orders1 = new ArrayList<>();
        Order order1 = new Order();
        ReflectionTestUtils.setField(order1, "id", 1L);
        order1.setProduct("Test Product 1");
        order1.setUser(user1);
        orders1.add(order1);
        user1.setOrders(orders1);
        
        User user2 = new User();
        ReflectionTestUtils.setField(user2, "id", 2L);
        user2.setName("Test User 2");
        user2.setOrders(new ArrayList<>());
        
        mockUsers.add(user1);
        mockUsers.add(user2);
        
        // Configure mocks
        when(userRepository.findAllWithOrders()).thenReturn(mockUsers);
        when(userRepository.findAll()).thenReturn(mockUsers);
    }

    @Test
    void testGetAllUsersBadPractice() {
        // Test the bad practice method that returns entities directly
        List<User> users = entityExposureService.getAllUsersBadPractice();
        
        // Verify the repository method was called
        verify(userRepository).findAllWithOrders();
        
        // Assertions
        assertEquals(2, users.size(), "Should return 2 users");
        
        // These entities could expose too much information and cause serialization issues
        User firstUser = users.get(0);
        assertEquals("Test User 1", firstUser.getName());
        
        // The bad practice exposes the full entity graph including relationships
        assertNotNull(firstUser.getOrders());
        assertFalse(firstUser.getOrders().isEmpty());
    }

    @Test
    void testGetAllUsersGoodPractice() {
        // Test the good practice method that returns DTOs
        List<UserDTO> userDTOs = entityExposureService.getAllUsersGoodPractice();
        
        // Verify the repository method was called
        verify(userRepository).findAllWithOrders();
        
        // Assertions
        assertEquals(2, userDTOs.size(), "Should return 2 user DTOs");
        
        // The DTO only contains the fields we explicitly mapped
        UserDTO firstUserDTO = userDTOs.get(0);
        assertEquals("Test User 1", firstUserDTO.getName());
        assertEquals(1, firstUserDTO.getOrderCount());
        
        // The DTO doesn't expose the entire entity relationship
        // It only contains what we explicitly included (orderCount in this case)
        // This prevents overfetching and protects internal domain model
    }
    
    @Test
    void testGetUsersWithN1Problem() {
        // Test the method that demonstrates N+1 problem
        List<User> users = entityExposureService.getUsersWithN1Problem();
        
        // Verify the repository method was called
        verify(userRepository).findAll();
        
        // Assertions
        assertEquals(2, users.size(), "Should return 2 users");
        
        // N+1 issue happens when accessing related collections after fetching parent entities
        // (In a real app, this would cause additional queries for each user)
    }
    
    @Test
    void testGetUsersWithoutN1Problem() {
        // Test the method that avoids N+1 problem
        List<User> users = entityExposureService.getUsersWithoutN1Problem();
        
        // Verify the repository method was called
        verify(userRepository).findAllWithOrders();
        
        // Assertions
        assertEquals(2, users.size(), "Should return 2 users");
        
        // This should fetch all users and their orders in a single query
        User firstUser = users.get(0);
        assertNotNull(firstUser.getOrders());
    }
}
