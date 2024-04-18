package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.config.PictureConfig;
import com.phoenix.blog.config.URLConfig;
import com.phoenix.blog.constant.HttpConstant;
import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.exceptions.InvalidateArgumentException;
import com.phoenix.blog.exceptions.PasswordErrorException;
import com.phoenix.blog.exceptions.UserNotFoundException;
import com.phoenix.blog.exceptions.UsernameExistException;
import com.phoenix.blog.core.mapper.UseMapper;
import com.phoenix.blog.core.service.UserService;
import com.phoenix.blog.util.DataUtil;
import com.phoenix.blog.util.PictureUtil;
import com.phoenix.blog.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UseMapper useMapper;
    @Autowired
    URLConfig urlConfig;
    @Autowired
    PictureConfig pictureConfig;

    @Override
    @Transactional
    public User getUser(String userId) {
        if (DataUtil.isEmptyData(userId)) throw new InvalidateArgumentException();
        User user = useMapper.selectOne(new QueryWrapper<User>().eq("user_id",userId));

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }

    @Override
    @Transactional
    public User register(UserRegisterDTO userRegisterDTO) {

        BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String avatarName = PictureUtil.saveOrUpdateFile(userRegisterDTO.getAvatarBase64(), null,pictureConfig.defaultAvatarPath,true);
        //Todo
        String avatarURL = HttpConstant.HTTP_PREFIX+urlConfig.getBaseURL()+"/"
                +pictureConfig.getDefaultAvatarPath()
                +avatarName;

        if (useMapper.selectOne(new QueryWrapper<User>().eq("username",username))!=null){
            throw new UsernameExistException();
        }

        User user = new User();
        DataUtil.setFields(user, userRegisterDTO, () ->
                user.setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setUserRole(userRegisterDTO.getRole())
                .setUserAvatarURL(avatarURL)
                .setRegisterTime(new Timestamp(System.currentTimeMillis())));


        useMapper.insert(user);

        return user;
    }

    @Override
    @Transactional
    public User login(UserLoginDTO userLoginDTO) {

        BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();

        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = useMapper.selectOne(new QueryWrapper<User>().eq("username",username));

        if (user == null){
            throw new UserNotFoundException();
        }

        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new PasswordErrorException();
        }

        return user;
    }
}
