package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EntityExposureService {
    private static final Logger logger = LoggerFactory.getLogger(EntityExposureService.class);
    private final UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // Constructor injection is preferred over field injection
    public EntityExposureService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    private void logQueryStatistics(String methodName) {
        try {
            Session session = entityManager.unwrap(Session.class);
            Statistics stats = session.getSessionFactory().getStatistics();
            
            logger.info("-------- {} Statistics --------", methodName);
            logger.info("Query execution count: {}", stats.getQueryExecutionCount());
            logger.info("Entity load count: {}", stats.getEntityLoadCount());
            logger.info("Collection load count: {}", stats.getCollectionLoadCount());
            logger.info("Collection fetch count: {}", stats.getCollectionFetchCount());
            logger.info("-----------------------------------");
        } catch (Exception e) {
            logger.warn("Could not log statistics: {}", e.getMessage());
        }
    }
    
    // BAD: Directly exposing entities in API responses
    public List<User> getAllUsersBadPractice() {
        // This exposes all fields from the entity, including relationships
        // Can lead to serialization issues and exposing internal data model
        return userRepository.findAllWithOrders();
    }
    
    // GOOD: Using DTOs to control what data is exposed
    public List<UserDTO> getAllUsersGoodPractice() {
        // Using DTOs to map only the data we want to expose
        List<User> users = userRepository.findAllWithOrders();
        return UserDTO.fromEntities(users);
    }
    
    // BAD: Demonstrating N+1 problem with default findAll() and lazy loading
    @Transactional
    public List<User> getUsersWithN1Problem() {
        logger.info("Starting getUsersWithN1Problem - demonstrating N+1 problem");
        
        // Reset statistics before execution
        entityManager.unwrap(Session.class).getSessionFactory().getStatistics().clear();
        
        // This uses default findAll which doesn't fetch orders eagerly
        List<User> users = userRepository.findAll();
        
        // Force access of the lazy-loaded collections to trigger N+1 queries
        for (User user : users) {
            if (user.getOrders() != null) {
                // This will cause a separate query for each user
                logger.info("User {} has {} orders", user.getName(), user.getOrders().size());
            }
        }
        
        // Log statistics showing multiple queries
        logQueryStatistics("getUsersWithN1Problem");
        
        return users;
    }
    
    // GOOD: Avoiding N+1 with JOIN FETCH
    @Transactional(readOnly = true)
    public List<User> getUsersWithoutN1Problem() {
        logger.info("Starting getUsersWithoutN1Problem - demonstrating N+1 solution");
        
        // Reset statistics before execution
        entityManager.unwrap(Session.class).getSessionFactory().getStatistics().clear();
        
        // This fetches users and their orders in a single query
        List<User> users = userRepository.findAllWithOrders();
        
        // Access the collections that are already loaded (no additional queries)
        for (User user : users) {
            if (user.getOrders() != null) {
                // This will NOT cause additional queries
                logger.info("User {} has {} orders", user.getName(), user.getOrders().size());
            }
        }
        
        // Log statistics showing single query
        logQueryStatistics("getUsersWithoutN1Problem");
        
        return users;
    }
}
