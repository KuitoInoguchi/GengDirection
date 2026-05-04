package com.nailong.gengdirection.common;

import lombok.Data;
/*
    全局返回结构体
 */
@Data
public class Result<T> {
    private Integer statusCode;
    private T data;
    private String message;

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setStatusCode(200);
        result.setData(data);
        result.setMessage("Success!");
        return result;
    }

    public static <T> Result<T> success(){
        return success(null);
    }

    public static <T> Result<T> error(Integer statusCode, String message){
        Result<T> result = new Result<>();
        result.setStatusCode(statusCode);
        result.setMessage(message);
        return result;
    }

}