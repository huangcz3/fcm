package com.asiainfo.fcm.mapper.primary;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/23.
 */
public interface ChannelEvalutionMapper {
    List<Map<String,Object>> getChannelEvalution(Map map);

    Map getChannelCountIndex(Map map);

    int getChannelEvalutionAllPage(Map<String, Object> map);

    List<Map<String,String>> getChannelEvalutionAll(Map map);
}
