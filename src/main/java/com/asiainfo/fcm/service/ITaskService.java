package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.Task;
import com.asiainfo.fcm.entity.TaskActivityRel;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/7/5.
 */
public interface ITaskService {
    long getActivityTotalRecord4Task(Map<String, Object> map);
    List<Activity> getActivity4Task(Map<String, Object> map);
    Task getTaskStartAndEndTime(List<Activity> activityList);
    void addTask(Task task);
    long getTasksTotalRecord(Map<String, Object> map);
    List<Task> getTasks(Map<String,Object> map);
    void deleteTask(String taskId);
    Task getTaskDetail(String taskId);
    List<TaskActivityRel> getRelativeActivities(String taskId);
    /**
     * 记录活动操作记录
     */
    void recordOpLog(Map map);

    /**
     * 修改task
     * @param task
     */
    void editTask(Task task);
    List<Map<String,String >> getTaskListDownload(Map map);
}
