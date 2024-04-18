package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.User;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVO {
    String username;
    String token;
    String avatarURL;

    public static UserVO BuildVO(User user, @Nullable String token) {
        return UserVO.builder()
                .username(user.getUsername())
                //Todo
                .avatarURL(user.getUserAvatarURL())
                .token(token)
                .build();
    }

}
