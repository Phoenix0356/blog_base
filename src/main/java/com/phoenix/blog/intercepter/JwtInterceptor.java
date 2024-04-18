package com.phoenix.blog.intercepter;

import com.phoenix.blog.config.JwtConfig;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.exceptions.JwtValidatingException;
import com.phoenix.blog.util.DataUtil;
import com.phoenix.blog.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (!DataUtil.isEmptyData(token) && token.startsWith("Bearer ")){
            token = token.substring(7);
            try {
                Claims claims = JwtUtil.isValidateToken(token,jwtConfig.secret);
                TokenContext.setClaims(claims);
            }catch (JwtException je){
                throw new JwtValidatingException();
            }
        }else {
            throw new JwtValidatingException("Please login");

        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
