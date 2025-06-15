package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper service for data processing operations.
 * This is extracted to a separate service bean for proper dependency injection 
 * instead of manual instantiation.
 * 
 * This class demonstrates several benefits of dependency injection:
 * 1. Environment-specific configuration (processingMode)
 * 2. Lifecycle management by Spring
 * 3. Easier to replace with different implementations
 * 4. Easier to test
 */
@Service
public class DataHelperService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataHelperService.class);
    
    @Value("${app.processing.mode:standard}")
    private String processingMode;
    
    public DataHelperService() {
        logger.info("DataHelperService created and managed by Spring");
    }
    
    public String process(long count) {
        switch (processingMode) {
            case "detailed":
                return "Processed " + count + " records in detailed mode";
            case "minimal":
                return "Processed: " + count;
            case "standard":
            default:
                return "Processed " + count + " records";
        }
    }
}
