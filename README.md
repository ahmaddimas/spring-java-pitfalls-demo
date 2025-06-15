# Spring Java Pitfalls Demo

This project demonstrates common pitfalls and best practices in Spring Boot and Java applications. Each example includes both a "bad practice" and "good practice" implementation with explanations.

## Running the Demo

1. Start the application: `./mvnw spring-boot:run`
2. Access the endpoints via browser or Postman at `http://localhost:8080/pitfalls/...`
3. Check the logs to see the effects of each pattern

## Pitfalls Covered

### 1. N+1 Query Problem

The N+1 query problem occurs when an application executes one query to retrieve a list of N records, and then executes N additional queries to retrieve related data for each of those records.

**Bad Practice:** Using default lazy loading without optimization
```java
List<User> users = userRepository.findAll(); // 1 query
for (User user : users) {
    List<Order> orders = user.getOrders(); // N additional queries
}
```

**Good Practice:** Using JOIN FETCH or EntityGraph
```java
@Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
List<User> findAllWithOrders();

// OR

@EntityGraph(attributePaths = {"orders"})
List<User> findAll();
```

### 2. Entity Exposure in APIs

**Bad Practice:** Directly returning entities in API responses
- Exposes internal data model
- Can cause serialization issues with lazy-loaded relationships
- Tightly couples API contract to database model

**Good Practice:** Using DTOs to control what data is exposed
- Separates internal model from external representation
- Controls exactly what data is exposed
- Allows for versioning and evolution of API independently of database

### 3. Overusing @Autowired

**Bad Practice:** Field injection with @Autowired
- Hidden dependencies
- Harder to test
- Cannot use final fields

**Good Practice:** Constructor injection
- Explicit dependencies
- Easy to test
- Can use final fields

### 4. Misusing @Transactional

**Bad Practice:** Using @Transactional for read operations or missing it for write operations
- Performance overhead for reads
- Data inconsistency for writes
- Partial operations that should be atomic

**Good Practice:** Proper transaction boundaries
- Use `readOnly=true` for read operations
- Ensure proper scope for transaction boundaries
- Understanding isolation levels and propagation behavior
