package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.exceptions.UsernameExistException;

import java.util.Date;

public interface UserService {
    public User register(UserRegisterDTO userRegisterDTO) throws UsernameExistException;

    public User login(UserLoginDTO userLoginDTO);

    public User getUser(String userId);

    public void logout(String jwtId, Date jwtExpirationTime);



}
