package com.phoenix.blog.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

//@Configuration
//public class ThreadPoolConfig {
//    //创建线程池
//    @Bean("taskExecutor")
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
//        pool.setThreadNamePrefix("--------------全局线程池-----------------");
//        pool.setCorePoolSize(16);
//        pool.setMaxPoolSize(8);
//        pool.setKeepAliveSeconds(5);
//        pool.setQueueCapacity(300);
//        // 直接在execute方法的调用线程中运行
//        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        // 初始化
//        pool.initialize();
//        return pool;
//    }
//}
