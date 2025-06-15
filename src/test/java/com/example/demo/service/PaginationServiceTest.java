package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PaginationServiceTest {

    @Mock
    private UserRepository userRepository;

    private PaginationService paginationService;
    private List<User> mockUsers;

    @BeforeEach
    void setUp() {
        paginationService = new PaginationService(userRepository);
        
        // Create a list of mock users
        mockUsers = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            User user = new User();
            org.springframework.test.util.ReflectionTestUtils.setField(user, "id", (long) i);
            user.setName("User " + i);
            mockUsers.add(user);
        }
        
        // Setup mock repository
        when(userRepository.findAll()).thenReturn(mockUsers);
        
        // Mock pagination behavior
        when(userRepository.findAll(any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), mockUsers.size());
            List<User> pageContent = mockUsers.subList(start, end);
            return new PageImpl<>(pageContent, pageable, mockUsers.size());
        });
    }

    @Test
    void testGetAllUsersBadPractice() {
        // Test the bad practice method (get all users at once)
        List<User> users = paginationService.getAllUsersBadPractice();
        
        // Verify all users are returned
        assertEquals(25, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUsersWithPaginationGoodPractice() {
        // Test the good practice method (paginated results)
        Page<User> page1 = paginationService.getUsersWithPaginationGoodPractice(0, 10);
        
        // Verify pagination works
        assertEquals(10, page1.getContent().size());
        assertEquals(25, page1.getTotalElements());
        assertEquals(3, page1.getTotalPages());
        
        // Verify sort was applied
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        verify(userRepository).findAll(eq(expectedPageable));
        
        // Test second page
        Page<User> page2 = paginationService.getUsersWithPaginationGoodPractice(1, 10);
        assertEquals(10, page2.getContent().size());
        
        // Test last page
        Page<User> page3 = paginationService.getUsersWithPaginationGoodPractice(2, 10);
        assertEquals(5, page3.getContent().size());
    }
}
