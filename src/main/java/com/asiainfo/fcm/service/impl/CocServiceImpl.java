package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.secondary.CocMapper;
import com.asiainfo.fcm.service.ICocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by RUOK on 2017/7/9.
 */
@Service
public class CocServiceImpl implements ICocService {

    @Autowired
    private CocMapper cocMapper;

    @Override
    public Timestamp getCocTimestamp() {
        return cocMapper.getCocTimestamp();
    }
}
