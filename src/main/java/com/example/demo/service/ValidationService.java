package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.dto.UserDTO;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.regex.Pattern;

@Service
@Validated // Enables method-level validation
public class ValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    
    // BAD: Reinventing validation logic
    public boolean isValidUserBadPractice(UserDTO user, String email) {
        // Manual validation - reinventing the wheel
        if (user == null) {
            return false;
        }
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }
        
        // Custom email validation that could have bugs
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }
        
        return true;
    }
    
    // GOOD: Using Spring's built-in validation
    public boolean isValidUserGoodPractice(@Valid UserDTO user, @NotBlank @Email String email) {
        // No need for manual validation - Spring's validation framework handles it
        // If validation fails, MethodArgumentNotValidException is thrown
        return true;
    }
    
    // BAD: Custom scheduled task implementation
    public void scheduledTaskBadPractice() {
        // Manually creating a thread for scheduled tasks
        Thread schedulerThread = new Thread(() -> {
            while (true) {
                try {
                    // Do some periodic task
                    System.out.println("Performing scheduled task");
                    Thread.sleep(60000); // Sleep for 1 minute
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }
    
    // GOOD: Using Spring's @Scheduled annotation (would be in a @Component class)
    // @Scheduled(fixedRate = 60000)
    public void scheduledTaskGoodPractice() {
        System.out.println("Performing scheduled task with Spring's scheduler");
    }
    
    /**
     * Simple validation method for string data.
     * Used by the ManualInstantiationService as an example of proper DI.
     * 
     * In a real application, this could use other injected services
     * or framework components for validation.
     */
    public boolean validate(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        
        // Additional validation rules could be added here
        // In a real app, we might use Bean Validation or a validator framework
        // that would itself be injected into this service
        return data.length() >= 3 && !data.contains("invalid");
    }
}
