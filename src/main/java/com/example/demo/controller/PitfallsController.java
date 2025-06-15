package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pitfalls")
public class PitfallsController {

    private final EntityExposureService entityExposureService;
    private final AutowiredService autowiredService;
    private final ConstructorInjectionService constructorInjectionService;
    private final TransactionService transactionService;
    
    public PitfallsController(
            EntityExposureService entityExposureService,
            AutowiredService autowiredService,
            ConstructorInjectionService constructorInjectionService,
            TransactionService transactionService) {
        this.entityExposureService = entityExposureService;
        this.autowiredService = autowiredService;
        this.constructorInjectionService = constructorInjectionService;
        this.transactionService = transactionService;
    }
    
    // Pitfall #1: Exposing Entities in APIs
    @GetMapping("/entity-exposure/bad")
    public List<User> getUsersExposingEntities() {
        return entityExposureService.getAllUsersBadPractice();
    }
    
    @GetMapping("/entity-exposure/good")
    public List<UserDTO> getUsersWithDTOs() {
        return entityExposureService.getAllUsersGoodPractice();
    }
    
    // Pitfall #2: Overusing @Autowired
    @GetMapping("/autowiring/bad")
    public ResponseEntity<String> getAutowiredBad() {
        return ResponseEntity.ok(autowiredService.getRepositoryStatusBadPractice());
    }
    
    @GetMapping("/autowiring/good")
    public ResponseEntity<String> getAutowiredGood() {
        return ResponseEntity.ok(constructorInjectionService.getRepositoryStatusGoodPractice());
    }
    
    // Pitfall #3: Misusing @Transactional
    @GetMapping("/transaction/bad/read/{id}")
    public ResponseEntity<User> getTransactionalReadBad(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getUserByIdBadPractice(id));
    }
    
    @GetMapping("/transaction/good/read/{id}")
    public ResponseEntity<User> getTransactionalReadGood(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getUserByIdGoodPractice(id));
    }
    
    @PostMapping("/transaction/bad/create")
    public ResponseEntity<String> createUserWithOrderBad(
            @RequestParam String userName, 
            @RequestParam String productName) {
        transactionService.createUserWithOrdersBadPractice(userName, productName);
        return ResponseEntity.ok("Created user and order (no transaction)");
    }
    
    @PostMapping("/transaction/good/create")
    public ResponseEntity<String> createUserWithOrderGood(
            @RequestParam String userName, 
            @RequestParam String productName) {
        transactionService.createUserWithOrdersGoodPractice(userName, productName);
        return ResponseEntity.ok("Created user and order (with transaction)");
    }
}
