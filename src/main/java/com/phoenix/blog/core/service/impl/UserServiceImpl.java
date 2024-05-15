package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.config.JwtConfig;
import com.phoenix.blog.config.PictureConfig;
import com.phoenix.blog.config.URLConfig;
import com.phoenix.blog.constant.HttpConstant;
import com.phoenix.blog.core.mapper.UserLogMapper;
import com.phoenix.blog.core.service.UserLogService;
import com.phoenix.blog.exceptions.clientException.*;
import com.phoenix.blog.model.dto.UserDTO;
import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.core.mapper.UserMapper;
import com.phoenix.blog.core.service.UserService;
import com.phoenix.blog.model.vo.UserVO;
import com.phoenix.blog.util.DataUtil;
import com.phoenix.blog.util.JwtUtil;
import com.phoenix.blog.util.PictureUtil;
import com.phoenix.blog.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserLogService userLogService;

    final StringRedisTemplate stringRedisTemplate;

    final UserMapper userMapper;
    final UserLogMapper userLogMapper;

    final URLConfig urlConfig;

    final PictureConfig pictureConfig;

    final JwtConfig jwtConfig;

    @Override
    @Transactional
    public UserVO getUserById(String userId) {
        if (DataUtil.isEmptyData(userId)) throw new InvalidateArgumentException();
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new UserNotFoundException();
        }
        return UserVO.BuildVO(user,null);
    }

    @Override
    @Transactional
    public UserVO getUserByUsername(String username){
        if (DataUtil.isEmptyData(username)) throw new InvalidateArgumentException();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));

        if (user == null){
            throw new UserNotFoundException();
        }
        return UserVO.BuildVO(user,null);
    }

    @Override
    @Transactional
    public UserVO updateUser(UserDTO userDTO,String userId) {
        if (DataUtil.isEmptyData(userId)) throw new InvalidateArgumentException();

        User user = userMapper.selectById(userId);

        if (user == null) throw new UserNotFoundException();

        String newUsername = userDTO.getUsername();
        DataUtil.setFields(user,userDTO,()->
                user.setUsername(newUsername));

        if (userMapper.selectOne(new QueryWrapper<User>().eq("username",newUsername))!=null){
            throw new UsernameExistException();
        }
        userMapper.updateById(user);

        return UserVO.BuildVO(user,null);
    }

    @Override
    @Transactional
    public UserVO register(UserRegisterDTO userRegisterDTO) {
        BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String avatarName = PictureUtil.saveOrUpdateFile(userRegisterDTO.getAvatarBase64(), null,pictureConfig.defaultAvatarPath,true);

        String avatarURL = HttpConstant.HTTPS_PREFIX+urlConfig.dnsName
                +pictureConfig.getDefaultAvatarURL()
                +avatarName;

        if (userMapper.selectOne(new QueryWrapper<User>().eq("username",username))!=null){
            throw new UsernameExistException();
        }

        User user = new User();
        DataUtil.setFields(user, userRegisterDTO, () ->
                user.setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setUserRole(userRegisterDTO.getRole())
                .setUserAvatarURL(avatarURL)
                .setRegisterTime(new Timestamp(System.currentTimeMillis())));

        userMapper.insert(user);
        String token = JwtUtil.getJwt(user.getUserId(), user.getUserRole().name(),
                jwtConfig.secret, jwtConfig.expiration);
        stringRedisTemplate.opsForValue().set(user.getUserId(),"",jwtConfig.expiration,TimeUnit.SECONDS);

        //记录日志
        userLogService.saveUserLog(user);
        return UserVO.BuildVO(user,token);
    }

    @Override
    @Transactional
    public UserVO login(UserLoginDTO userLoginDTO) {

        BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();

        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
        if (user == null){
            throw new UserNotFoundException();
        }
        if (stringRedisTemplate.opsForValue().get(user.getUserId())!=null){
            throw new UserAlreadyLoginException();
        }
        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new PasswordErrorException();
        }

        String token = JwtUtil.getJwt(user.getUserId(), user.getUserRole().name(),
                jwtConfig.secret, jwtConfig.expiration);
        stringRedisTemplate.opsForValue().set(user.getUserId(),"",jwtConfig.expiration,TimeUnit.SECONDS);

        //记录日志
        userLogService.saveUserLog(user);

        return UserVO.BuildVO(user,token);
    }

    @Override
    @Transactional
    public void logout(String jwtId, String userId, Date jwtExpirationTime) {
        long expTime = Math.max(jwtExpirationTime.getTime()-System.currentTimeMillis(),0);
        stringRedisTemplate.opsForValue().set(jwtId,"",expTime/1000, TimeUnit.SECONDS);
        stringRedisTemplate.delete(userId);
    }
}
