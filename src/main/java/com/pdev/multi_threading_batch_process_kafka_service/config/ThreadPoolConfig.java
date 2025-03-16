package com.pdev.multi_threading_batch_process_kafka_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {

    /**
     * 10 threads start immediately when tasks arrive.
     * If more than 10 tasks arrive, they go into the queue (up to 20).
     * If the queue is full, new threads are created (up to 50 max).
     * If 50 threads are running AND the queue is full, new tasks get rejected.
     */
    @Bean
    public Executor createSmsExecutorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);   // Core threads
        executor.setMaxPoolSize(50);    // Max threads
        executor.setQueueCapacity(1000); // Queue capacity
        executor.setThreadNamePrefix("SmsSender-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor createEmailExecutorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // Core threads
        executor.setMaxPoolSize(20);   // Max threads
        executor.setQueueCapacity(500); // Queue capacity
        executor.setThreadNamePrefix("EmailSender-");
        executor.initialize();
        return executor;
    }
}
