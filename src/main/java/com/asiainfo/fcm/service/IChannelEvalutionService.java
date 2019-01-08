package com.asiainfo.fcm.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/23.
 */
public interface IChannelEvalutionService {

    List<Map<String,Object>> getChannelEvalution(Map map);

    Map getChannelCountIndex(Map map);

    int getChannelEvalutionAllPage(Map<String, Object> map);

    List<Map<String,String>> getChannelEvalutionAll(Map map);
}
