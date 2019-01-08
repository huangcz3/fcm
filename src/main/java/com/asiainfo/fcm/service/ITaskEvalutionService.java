package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.TaskEvalution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/6.
 */
public interface ITaskEvalutionService {
    List<TaskEvalution> getTaskEvalutionList(Map parameterMap);
    int getTaskEvalutionListTotals(Map parameterMap);
    List<Map<String,String>> getAllTaskEvalutionList(Map map);
}
