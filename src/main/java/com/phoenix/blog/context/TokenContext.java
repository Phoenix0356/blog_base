package com.phoenix.blog.context;

import com.phoenix.blog.constant.JwtConstant;
import io.jsonwebtoken.Claims;

//Todo
//aop删除threadLocal
public class TokenContext {
    private static final ThreadLocal<Claims> threadLocal = new ThreadLocal<>();

    public static void setClaims(Claims claims){

        threadLocal.set(claims);
    }
    public static String getUserId(){
        return (String) threadLocal.get().get(JwtConstant.SUBTITLE);
    }

    public static String getUserRole(){
        return (String) threadLocal.get().get(JwtConstant.ROLE);
    }

    public static void removeClaims(){
        threadLocal.remove();
    }
}
