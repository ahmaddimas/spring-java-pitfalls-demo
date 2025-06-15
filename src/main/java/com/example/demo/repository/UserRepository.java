package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // BAD: N+1 Problem (default findAll)

    // GOOD: Avoid N+1 with JOIN FETCH
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
    List<User> findAllWithOrders();
    
    // ALTERNATIVE: Using EntityGraph to load relations
    // @EntityGraph(attributePaths = {"orders"})
    // List<User> findAll();
}