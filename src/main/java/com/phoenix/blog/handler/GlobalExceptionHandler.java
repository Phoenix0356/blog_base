package com.phoenix.blog.handler;

import com.phoenix.blog.exceptions.userException.BaseException;
import com.phoenix.blog.model.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResultVO exceptionHandler(BaseException ex){

        return ResultVO.error(ex.getMessage());
    }
}
