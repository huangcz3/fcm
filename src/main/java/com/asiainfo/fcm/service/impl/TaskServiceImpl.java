package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.Task;
import com.asiainfo.fcm.entity.TaskActivityRel;
import com.asiainfo.fcm.mapper.primary.TaskMapper;
import com.asiainfo.fcm.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/7/5.
 */
@Transactional(readOnly = true)
@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public long getActivityTotalRecord4Task(Map<String, Object> map) {
        return taskMapper.getActivityTotalRecord4Task(map);
    }

    @Override
    public List<Activity> getActivity4Task(Map<String, Object> map) {
        return taskMapper.getActivity4Task(map);
    }

    @Override
    public Task getTaskStartAndEndTime(List<Activity> activityList) {
        return taskMapper.getTaskStartAndEndTime(activityList);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addTask(Task task) {

        taskMapper.addTask(task);

        if (task.getActivityList().size() > 0) {
            taskMapper.addTaskActivity(task);
        }
    }

    @Override
    public long getTasksTotalRecord(Map<String, Object> map) {
        return taskMapper.getTasksTotalRecord(map);
    }

    @Override
    public List<Task> getTasks(Map<String, Object> map) {

        String userId = map.get("userId").toString();
        List<Task> taskList = taskMapper.getTasks(map);

        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getCreatorId().equals(userId)) {
                taskList.get(i).setIsCanDelete(1);
            } else {
                taskList.get(i).setIsCanDelete(0);
            }

            String taskId = taskList.get(i).getTaskId();
            int relativeNum = taskMapper.getRelativeActivityNum(taskId);
            taskList.get(i).setRelativeActivityNum(relativeNum);
        }
        return taskList;

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteTask(String taskId) {
        taskMapper.deleteTask(taskId);
        taskMapper.deleteTaskActivity(taskId);
    }

   @Override
    public Task getTaskDetail(String taskId){
        return taskMapper.getTaskDetail(taskId);
    }
    @Override
    public  List<TaskActivityRel> getRelativeActivities(String taskId){
        return taskMapper.getRelativeActivities(taskId);
    }

    @Override
    public void recordOpLog(Map map) {
        taskMapper.recordOplog(map);
    }

    @Override
    public void editTask(Task task) {
        taskMapper.editTask(task);
        taskMapper.deleteTaskActivity(task.getTaskId());
        if (task.getActivityList().size() > 0) {
            taskMapper.addTaskActivity(task);
        }
    }

    @Override
    public List<Map<String,String>> getTaskListDownload(Map map){

        List<Map<String,String>> taskList = taskMapper.getTaskListDownload(map);

        for (int i = 0; i < taskList.size(); i++) {
            String taskId = taskList.get(i).get("TASK_ID");
            int relativeNum = taskMapper.getRelativeActivityNum(taskId);
            taskList.get(i).put("RELATIVENUM",Integer.toString(relativeNum));
        }
        return taskList;
    }
}
