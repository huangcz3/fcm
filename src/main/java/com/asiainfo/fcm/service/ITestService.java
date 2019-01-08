package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.Test;

import java.util.List;
import java.util.Map;

public interface ITestService {
    List<Test> getValue();

    void addValue(Map<String, String> map) throws Exception;

    void insertTestData(Map map);

    void udateState(String id);
}
