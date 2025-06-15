package com.example.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // The @EnableCaching annotation activates Spring's caching mechanism
    // In a real app, you'd configure the cache manager here
}
