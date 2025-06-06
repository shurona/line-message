package com.shrona.line_demo.common.config;

import static com.shrona.line_demo.common.utils.StaticVariable.POOL_SIZE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    /**
     * TaskScheduler 설정
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(POOL_SIZE);
        scheduler.setThreadNamePrefix("TASK-SCHEDULER-");
        scheduler.initialize();
        return scheduler;
    }

}
