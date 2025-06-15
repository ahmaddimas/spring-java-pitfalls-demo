package com.example.demo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.entity.User;
import com.example.demo.service.AutowiredService;
import com.example.demo.service.ConstructorInjectionService;
import com.example.demo.service.EntityExposureService;
import com.example.demo.service.TransactionService;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(PitfallsController.class)
class PitfallsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntityExposureService entityExposureService;
    
    @MockBean
    private AutowiredService autowiredService;
    
    @MockBean
    private ConstructorInjectionService constructorInjectionService;
    
    @MockBean
    private TransactionService transactionService;
    
    @MockBean
    private UserRepository userRepository;
    
    @MockBean
    private OrderRepository orderRepository;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testEntityExposureEndpoint() throws Exception {
        // Mock the service methods
        List<User> mockUsers = new ArrayList<>();
        when(entityExposureService.getAllUsersBadPractice()).thenReturn(mockUsers);
        
        // Test endpoint
        mockMvc.perform(get("/pitfalls/entity-exposure/bad"))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testAutowiredEndpoint() throws Exception {
        when(autowiredService.getRepositoryStatusBadPractice()).thenReturn("mocked status");
        
        mockMvc.perform(get("/pitfalls/autowiring/bad"))
            .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testTransactionEndpoint() throws Exception {
        User mockUser = new User();
        when(transactionService.getUserByIdBadPractice(1L)).thenReturn(mockUser);
        
        mockMvc.perform(get("/pitfalls/transaction/bad/read/1"))
            .andExpect(status().isOk());
    }
}
