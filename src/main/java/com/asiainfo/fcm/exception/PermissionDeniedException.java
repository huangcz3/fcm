package com.asiainfo.fcm.exception;

import com.asiainfo.fcm.enums.ResultEnum;

/**
 * 操作未授权异常.
 * Created by RUOK on 2017/6/27.
 */
public class PermissionDeniedException extends RuntimeException {

    private Integer code;

    private String msg;

    public PermissionDeniedException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
