package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.config.JwtConfig;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.UserService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.util.JwtUtil;
import com.phoenix.blog.model.vo.ResultVO;
import com.phoenix.blog.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    @GetMapping("/get")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getUser(){
        User user;
        try {
            user = userService.getUser(TokenContext.getUserId());
        }finally {
            TokenContext.removeClaims();
        }
        return ResultVO.success("Get user info success",UserVO.BuildVO(user,null));
    }
    @PostMapping("/register")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO register(@RequestBody UserRegisterDTO userRegisterDTO){
        User user = userService.register(userRegisterDTO);

        String token = JwtUtil.getJwt(user.getUserId(), user.getUserRole().name(),
                jwtConfig.secret, jwtConfig.expiration);

        return ResultVO.success("Register success",UserVO.BuildVO(user,token));
    }
    @PostMapping("/login")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request){
        User user = userService.login(userLoginDTO);
        String token = JwtUtil.getJwt(user.getUserId(), user.getUserRole().name(),
                jwtConfig.secret, jwtConfig.expiration);

        return ResultVO.success("Login success",UserVO.BuildVO(user,token));
    }

    @PostMapping("/logout")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO logout(){
        //Todo
        return null;
    }
}
