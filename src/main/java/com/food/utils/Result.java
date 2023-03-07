package com.food.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result<T> {
    /**
     * 状态代码:
     * 1:成功
     * 0和其他数字为失败
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    /**
     * 动态数据
     */
    private Map<String,String> map = new HashMap<>();

    public Result(T data) {
        this.code = 1;
        this.msg = null;
        this.data = data;
    }
    public Result(String msg) {
        this.code = 0;
        this.msg = msg;
        this.data = null;
    }
    public Result(T data,String msg) {
        this.code = 1;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result<>(data);
    }
    public static <T> Result<T> fail(String msg){
        return new Result<>(msg);
    }
    public static <T> Result<T> success(T data,String msg){
        return new Result<>(data,msg);
    }
}
