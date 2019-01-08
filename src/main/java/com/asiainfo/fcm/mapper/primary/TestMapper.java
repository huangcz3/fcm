package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.Test;

import java.util.List;
import java.util.Map;

public interface TestMapper {
    List<Test> getValue();

    void addValue(Map<String, String> map);

    void insertTestData(Map map);

    void updateState(String id);
}
