package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
class DataHelperServiceTest {

    @Autowired
    private DataHelperService dataHelperService;
    
    @Test
    void testProcessWithStandardMode() {
        // With default configuration
        String result = dataHelperService.process(50);
        assertEquals("Processed 50 records", result);
    }
}

/**
 * This test class demonstrates how dependency injection makes it easy
 * to test different configurations without changing the code.
 */
@SpringBootTest
@TestPropertySource(properties = {"app.processing.mode=detailed"})
class DataHelperServiceWithDetailedModeTest {

    @Autowired
    private DataHelperService dataHelperService;
    
    @Test
    void testProcessWithDetailedMode() {
        // With detailed mode configuration
        String result = dataHelperService.process(50);
        assertEquals("Processed 50 records in detailed mode", result);
    }
}

/**
 * Another test class with a different configuration
 */
@SpringBootTest
@TestPropertySource(properties = {"app.processing.mode=minimal"})
class DataHelperServiceWithMinimalModeTest {

    @Autowired
    private DataHelperService dataHelperService;
    
    @Test
    void testProcessWithMinimalMode() {
        // With minimal mode configuration
        String result = dataHelperService.process(50);
        assertEquals("Processed: 50", result);
    }
}
