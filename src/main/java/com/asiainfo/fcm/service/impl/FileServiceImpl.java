package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.UploadFile;
import com.asiainfo.fcm.mapper.primary.FileMapper;
import com.asiainfo.fcm.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by RUOK on 2017/6/27.
 */
@Service
@Transactional(readOnly = true)
public class FileServiceImpl implements IFileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    @Transactional(value = "primaryTransactionManager", readOnly = false, rollbackFor = Exception.class)
    public void saveUploadFile(UploadFile uploadFile) {
        fileMapper.saveUploadFile(uploadFile);
    }

    @Override
    public UploadFile getUploadFile(String fileName) {
        return fileMapper.getUploadFile(fileName);
    }

    @Override
    @Transactional(value = "primaryTransactionManager", readOnly = false, rollbackFor = Exception.class)
    public void addActivityImgRel(Map<String, Object> map) {
        fileMapper.addActivityImgRel(map);
    }

    @Override
    @Transactional(value = "primaryTransactionManager", readOnly = false, rollbackFor = Exception.class)
    public void addActivityImg(Map<String, Object> map) {
        fileMapper.addActivityImg(map);
    }
}
