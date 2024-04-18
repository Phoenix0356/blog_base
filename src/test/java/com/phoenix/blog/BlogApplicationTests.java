package com.phoenix.blog;

import com.phoenix.blog.core.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("dev")
class BlogApplicationTests {
    @Autowired
    UserController userController;
    @Test
    void contextLoads() {

    }

    @Test
    void testAop(){
        userController.getUser();

    }
}
