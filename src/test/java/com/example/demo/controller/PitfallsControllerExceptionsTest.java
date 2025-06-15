package com.example.demo.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.entity.User;
import com.example.demo.service.AutowiredService;
import com.example.demo.service.CachingService;
import com.example.demo.service.ConstructorInjectionService;
import com.example.demo.service.EntityExposureService;
import com.example.demo.service.EnvironmentConfigService;
import com.example.demo.service.ExceptionHandlingService;
import com.example.demo.service.ExceptionHandlingService.ResourceNotFoundException;
import com.example.demo.service.ManualInstantiationService;
import com.example.demo.service.PaginationService;
import com.example.demo.service.ScheduledTasksService;
import com.example.demo.service.SecurityPitfallsService;
import com.example.demo.service.TransactionService;
import com.example.demo.service.ValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

@WebMvcTest(PitfallsController.class)
class PitfallsControllerExceptionsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExceptionHandlingService exceptionHandlingService;
    
    // Need to mock all services used in the controller
    @MockBean private EntityExposureService entityExposureService;
    @MockBean private CachingService cachingService;
    @MockBean private EnvironmentConfigService environmentConfigService;
    @MockBean private AutowiredService autowiredService;
    @MockBean private ConstructorInjectionService constructorInjectionService;
    @MockBean private ManualInstantiationService manualInstantiationService;
    @MockBean private ValidationService validationService;
    @MockBean private TransactionService transactionService;
    @MockBean private ScheduledTasksService scheduledTasksService;
    @MockBean private SecurityPitfallsService securityPitfallsService;
    @MockBean private PaginationService paginationService;
    
    // Add missing repository that might be required by some services
    @MockBean private com.example.demo.repository.UserRepository userRepository;

    @Test
    void testExceptionHandling_BadPractice_UserNotFound() throws Exception {
        // Setup mock to return null for a non-existent user
        when(exceptionHandlingService.getUserByIdBadPractice(99L)).thenReturn(null);

        // Test bad practice - ad-hoc exception handling in controller
        mockMvc.perform(get("/pitfalls/exception-handling/bad/99"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(""));  // Empty response body with 404
    }

    @Test
    void testExceptionHandling_GoodPractice_UserNotFound() throws Exception {
        // Setup mock to throw ResourceNotFoundException
        when(exceptionHandlingService.getUserByIdGoodPractice(99L))
            .thenThrow(new ResourceNotFoundException("User not found with id: 99"));

        // Test good practice - global exception handling
        mockMvc.perform(get("/pitfalls/exception-handling/good/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is(404)))
            .andExpect(jsonPath("$.error", is("Not Found")))
            .andExpect(jsonPath("$.message", containsString("User not found with id: 99")));
    }

    @Test
    void testExceptionHandling_GoodPractice_UserFound() throws Exception {
        // Create mock user
        User mockUser = new User();
        org.springframework.test.util.ReflectionTestUtils.setField(mockUser, "id", 1L);
        mockUser.setName("Test User");
        
        // Setup mock to return user
        when(exceptionHandlingService.getUserByIdGoodPractice(1L)).thenReturn(mockUser);

        // Test good practice - successful response
        mockMvc.perform(get("/pitfalls/exception-handling/good/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Test User")));
    }
}
