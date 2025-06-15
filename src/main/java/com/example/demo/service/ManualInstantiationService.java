package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ManualInstantiationService {
    
    private final UserRepository userRepository;
    private final DataHelperService dataHelperService;
    private final ValidationService validationService;
    
    // GOOD: Constructor injection of all dependencies
    public ManualInstantiationService(
            UserRepository userRepository,
            DataHelperService dataHelperService,
            ValidationService validationService) {
        this.userRepository = userRepository;
        this.dataHelperService = dataHelperService;
        this.validationService = validationService;
    }
    
    // Using properly injected dependencies
    public String processData() {
        boolean isValid = validationService.validate("some data");
        if (isValid) {
            return dataHelperService.process(userRepository.count());
        }
        return "Invalid data";
    }
}
