package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.UploadFile;

import java.util.Map;

/**
 * Created by RUOK on 2017/6/27.
 */
public interface FileMapper {
    void saveUploadFile(UploadFile uploadFile);

    UploadFile getUploadFile(String fileName);

    void addActivityImgRel(Map<String,Object> map);

    void addActivityImg(Map<String,Object> map);
}
