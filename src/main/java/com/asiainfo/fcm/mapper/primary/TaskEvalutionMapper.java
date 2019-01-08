package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.TaskEvalution;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/7.
 */
public interface TaskEvalutionMapper {
    List<TaskEvalution> getTaskEvalutionList(Map parameterMap);
    int getTaskEvalutionListTotals(Map parameterMap);
    List<Map<String,String>> getAllTaskEvalutionList(Map map);
}
