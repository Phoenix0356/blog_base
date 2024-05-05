package com.phoenix.blog;

import com.phoenix.blog.core.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;

@SpringBootTest
@ActiveProfiles("dev")
class BlogApplicationTests {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    void contextLoads() {

    }
}
