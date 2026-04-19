package com.nailong.gengdirection.exception;

import com.nailong.gengdirection.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/*
    全局异常Handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GengException.class)
    public Result<Void> handleGengException(GengException e){
        log.warn("GengException:{}", e.getMessage());
        return Result.error(e.getStatusCode(), e.getMessage());

    }
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e){
        log.warn("Exception(From java.lang):{}", e.getMessage());
        return Result.error(500, "Internal Server Error");

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", msg);
        return Result.error(400, msg);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String msg = String.format("参数 '%s' 类型错误：期望 %s，实际收到 '%s'",
                e.getName(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "?",
                e.getValue());
        log.warn(msg);
        return Result.error(400, msg);
    }
}
