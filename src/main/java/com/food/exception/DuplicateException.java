package com.food.exception;

/**
 * 自定义异常:账号重复异常
 */
public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super(message);
    }
}
