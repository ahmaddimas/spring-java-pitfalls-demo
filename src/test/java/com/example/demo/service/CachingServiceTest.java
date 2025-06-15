package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class CachingServiceTest {

    @Autowired
    private CachingService cachingService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        // Clear any existing cache
        cacheManager.getCacheNames().forEach(cacheName -> {
            cacheManager.getCache(cacheName).clear();
        });
        
        // We don't need to reset userRepository since it's not a mock
    }
    
    @Test
    void testGetAllUsersBadPractice() {
        // Spy on the real userRepository to verify calls
        UserRepository repoSpy = spy(userRepository);
        CachingService serviceSpy = new CachingService(repoSpy);
        
        // Call method multiple times
        serviceSpy.getAllUsersBadPractice();
        serviceSpy.getAllUsersBadPractice();
        serviceSpy.getAllUsersBadPractice();
        
        // Verify that database is hit every time (no caching)
        verify(repoSpy, times(3)).findAllWithOrders();
    }
    
    @Test
    void testGetAllUsersGoodPractice() {
        // This test requires Spring's caching infrastructure to be set up
        // We'll test that the repository is only called once due to caching
        
        // Call the method multiple times
        List<User> firstCall = cachingService.getAllUsersGoodPractice();
        List<User> secondCall = cachingService.getAllUsersGoodPractice();
        List<User> thirdCall = cachingService.getAllUsersGoodPractice();
        
        // Verify we got the same object reference on subsequent calls
        // which indicates it came from cache
        assertSame(firstCall, secondCall);
        assertSame(firstCall, thirdCall);
        
        // Verify the cache exists
        assertNotNull(cacheManager.getCache("users"));
    }
    
    @Test
    void testGetUserByIdGoodPractice() {
        // Test caching with a specific key
        User user1 = cachingService.getUserByIdGoodPractice(1L);
        User user1Cached = cachingService.getUserByIdGoodPractice(1L);
        
        // Different ID should hit database
        User user2 = cachingService.getUserByIdGoodPractice(2L);
        User user2Cached = cachingService.getUserByIdGoodPractice(2L);
        
        // Verify the cache exists
        assertNotNull(cacheManager.getCache("userById"));
        
        // Both users should be in cache now
        assertNotNull(cacheManager.getCache("userById").get(1L));
        assertNotNull(cacheManager.getCache("userById").get(2L));
    }
}
