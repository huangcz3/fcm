package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.Task;
import com.asiainfo.fcm.entity.TaskActivityRel;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/7/5.
 */
public interface TaskMapper {
    long getActivityTotalRecord4Task(Map<String, Object> map);
    List<Activity> getActivity4Task(Map<String, Object> map);
    Task getTaskStartAndEndTime(List<Activity> activityList);
    void addTask(Task task);
    void addTaskActivity(Task task);
    long getTasksTotalRecord(Map<String, Object> map);
    List<Task> getTasks(Map<String, Object> map);
    int getRelativeActivityNum(String taskId);
    void deleteTask(String taskId);
    Task getTaskDetail(String taskId);
    List<TaskActivityRel> getRelativeActivities(String taskId);

    void recordOplog(Map map);

    void editTask(Task task);

    void deleteTaskActivity(String taskId);

    List<Map<String,String >> getTaskListDownload(Map map);
}
