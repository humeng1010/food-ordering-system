package com.food.exception;

import com.food.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e){
        return Result.fail("业务异常:"+e.getMessage());
    }

    /**
     * 账号重复异常
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateException.class)
    public Result<String> duplicateExceptionHandler(DuplicateException e){
        return Result.fail(e.getMessage());
    }

}
