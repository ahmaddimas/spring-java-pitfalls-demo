package com.example.demo;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests that verify the application is wired up correctly
 * and all controllers and services are working together.
 */
@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Verify application context loads successfully
    }
    
    @Test
    void testManualInstantiationEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/pitfalls/instantiation"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andReturn();
            
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Processed");
    }
    
    @Test
    void testEntityExposureEndpoints() throws Exception {
        // Test bad practice endpoint - returns entities directly
        mockMvc.perform(get("/pitfalls/entity-exposure/bad"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
            
        // Test good practice endpoint - returns DTOs
        mockMvc.perform(get("/pitfalls/entity-exposure/good"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
    
    @Test
    void testPaginationEndpoints() throws Exception {
        // Test pagination endpoint with custom page parameters
        mockMvc.perform(get("/pitfalls/pagination/good")
                .param("page", "0")
                .param("size", "5"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size").value(5))
            .andExpect(jsonPath("$.number").value(0));
    }
}
