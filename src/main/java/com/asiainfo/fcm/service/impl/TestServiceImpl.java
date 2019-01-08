package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.Test;
import com.asiainfo.fcm.mapper.primary.TestMapper;
import com.asiainfo.fcm.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TestServiceImpl implements ITestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public List<Test> getValue() {
        return testMapper.getValue();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void addValue(Map<String, String> map) throws Exception {
        testMapper.addValue(map);
//		throw new RuntimeException("aaa");
    }

    @Override
    public void udateState(String id) {
         testMapper.updateState(id);
    }

    @Override
   public void insertTestData(Map map) {
//       testMapper.insertTestData(map);
   }
}
