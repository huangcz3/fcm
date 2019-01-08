package com.asiainfo.fcm.exception;

import com.asiainfo.fcm.enums.ResultEnum;

/**
 * 上传文件类型不匹配异常.
 * Created by RUOK on 2017/6/28.
 */
public class FileTypeNotMatchException extends RuntimeException {
    private Integer code;

    private String msg;

    public FileTypeNotMatchException(ResultEnum resultEnum) {
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
