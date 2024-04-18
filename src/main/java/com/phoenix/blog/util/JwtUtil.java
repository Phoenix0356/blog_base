package com.phoenix.blog.util;

import com.phoenix.blog.constant.JwtConstant;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.HashMap;


public class JwtUtil {

    public static String getJwt(String userId,String role,String secret,long expiration){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtConstant.ISSUER,"phoenix");
        claims.put(JwtConstant.SUBTITLE,userId);
        claims.put(JwtConstant.ROLE,role);
        claims.put(JwtConstant.EXPIRATION,System.currentTimeMillis()+expiration);
        return Jwts.builder()
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

    }


    public static Claims isValidateToken(String token,String secret) throws JwtException {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
