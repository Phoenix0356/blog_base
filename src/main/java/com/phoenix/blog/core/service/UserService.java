package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.UserDTO;
import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.exceptions.UsernameExistException;
import com.phoenix.blog.model.vo.UserVO;

import java.util.Date;

public interface UserService {
    public UserVO register(UserRegisterDTO userRegisterDTO) throws UsernameExistException;

    public UserVO login(UserLoginDTO userLoginDTO);

    public UserVO getUser(String userId);

    public UserVO updateUser(UserDTO userDTO,String userId);

    public void logout(String jwtId, Date jwtExpirationTime);



}
