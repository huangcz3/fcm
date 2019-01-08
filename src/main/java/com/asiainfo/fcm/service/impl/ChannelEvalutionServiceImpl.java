package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.primary.ChannelEvalutionMapper;
import com.asiainfo.fcm.service.IChannelEvalutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/23.
 */
@Service
public class ChannelEvalutionServiceImpl implements IChannelEvalutionService {
    @Autowired
    private ChannelEvalutionMapper channelEvalutionMapper;

    @Override
    public List<Map<String, Object>> getChannelEvalution(Map map) {
        return channelEvalutionMapper.getChannelEvalution(map);
    }

    @Override
    public Map getChannelCountIndex(Map map) {
        return channelEvalutionMapper.getChannelCountIndex(map);
    }

    @Override
    public int getChannelEvalutionAllPage(Map<String, Object> map) {
        return channelEvalutionMapper.getChannelEvalutionAllPage(map);
    }

    @Override
    public List<Map<String, String>> getChannelEvalutionAll(Map map) {
        return channelEvalutionMapper.getChannelEvalutionAll(map);
    }
}
