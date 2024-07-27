package com.phoenix.blog.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.UserMapper;
import com.phoenix.blog.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserManager {
    final UserMapper userMapper;
    public User select(String userId){
        return userMapper.selectById(userId);
    }

    public List<User> selectListByUserId(List<String> userIdList){
        return userMapper.selectList(new QueryWrapper<User>().in("user_id",userIdList));
    }
}
