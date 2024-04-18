package com.phoenix.blog.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVO {
    String msg;
    Object object;
    int result;

    public static ResultVO success(String message, Object object){
        return new ResultVO(message,object,1);
    }
    public static ResultVO success(String message){
        return new ResultVO(message,null,1);
    }


    public static ResultVO error(String message, Object object, int statusCode){
        return new ResultVO(message,object,0);
    }

    public static ResultVO error(String message){
        return new ResultVO(message,null,0);
    }
}
