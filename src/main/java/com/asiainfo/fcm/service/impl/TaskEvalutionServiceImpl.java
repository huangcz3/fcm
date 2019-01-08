package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.TaskEvalution;
import com.asiainfo.fcm.mapper.primary.TaskEvalutionMapper;
import com.asiainfo.fcm.service.ITaskEvalutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJY on 2017/11/7.
 */
@Service
@Transactional
public class TaskEvalutionServiceImpl implements ITaskEvalutionService{

    @Autowired
    private TaskEvalutionMapper taskEvalutionMapper;

    @Override
    public List<TaskEvalution> getTaskEvalutionList(Map parameterMap){
        return taskEvalutionMapper.getTaskEvalutionList(parameterMap);
    }

    @Override
    public int getTaskEvalutionListTotals(Map parameterMap){
        return taskEvalutionMapper.getTaskEvalutionListTotals(parameterMap);
    }

    @Override
    public List<Map<String ,String>> getAllTaskEvalutionList(Map map){
        return taskEvalutionMapper.getAllTaskEvalutionList(map);
    }
}
