package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ManualInstantiationServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private DataHelperService dataHelperService;
    
    @Mock
    private ValidationService validationService;
    
    private ManualInstantiationService manualInstantiationService;
    
    @BeforeEach
    void setUp() {
        manualInstantiationService = new ManualInstantiationService(
            userRepository, dataHelperService, validationService);
            
        // Set up mocks
        when(userRepository.count()).thenReturn(10L);
        
        // Mock the validation with the updated logic
        // In a real test, this makes it easier to control behavior
        // without worrying about the actual validation implementation
        when(validationService.validate("some data")).thenReturn(true);
        
        when(dataHelperService.process(10L)).thenReturn("Processed 10 records");
    }
    
    @Test
    void testProcessData_WithValidData() {
        // Execute
        String result = manualInstantiationService.processData();
        
        // Verify
        assertEquals("Processed 10 records", result);
        verify(validationService).validate("some data");
        verify(dataHelperService).process(10L);
        verify(userRepository).count();
    }
    
    @Test
    void testProcessData_WithInvalidData() {
        // Change mock behavior to return false for validation
        when(validationService.validate(anyString())).thenReturn(false);
        
        // Execute
        String result = manualInstantiationService.processData();
        
        // Verify
        assertEquals("Invalid data", result);
        verify(validationService).validate("some data");
        verify(dataHelperService, never()).process(anyLong());
    }
    
    @Test
    void demonstrateBenefitsOfDependencyInjection() {
        // This test demonstrates how easy it is to test a class when using dependency injection
        // We can provide mock implementations that behave in specific ways for testing
        
        // Create a special test implementation with different behavior
        DataHelperService customHelper = mock(DataHelperService.class);
        when(customHelper.process(anyLong())).thenReturn("Custom processed result");
        
        // Create a new instance with our custom dependency
        ManualInstantiationService customService = new ManualInstantiationService(
            userRepository, customHelper, validationService);
        
        // Execute
        String result = customService.processData();
        
        // Verify custom behavior
        assertEquals("Custom processed result", result);
        verify(customHelper).process(10L);
    }
}
