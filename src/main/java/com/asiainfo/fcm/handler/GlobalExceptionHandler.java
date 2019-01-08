package com.asiainfo.fcm.handler;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理.
 * Created by RUOK on 2017/6/15.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) {

        logger.error("【系统异常】{}", e);

        if (e instanceof NoHandlerFoundException) {
//            return ResultUtil.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        } else if (e instanceof MultipartException) {
            return ResultUtil.error(ResultEnum.MAX_UPLOAD_SIZE_EXCEEDED);
        } else {
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }
}
