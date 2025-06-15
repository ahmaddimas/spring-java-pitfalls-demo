package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    // GOOD: Use a dedicated thread pool for scheduled tasks
    // This prevents scheduled tasks from blocking each other
    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(3); // Configure pool size based on needs
    }
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
}
