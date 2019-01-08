package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.primary.AreaEvaluationMapper;
import com.asiainfo.fcm.service.IAreaEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AreaEvaluationServiceImpl implements IAreaEvaluationService {

	@Autowired
	AreaEvaluationMapper areaEvaluationMapper;
	
	@Override
	public List<Map<String, Object>> getCityEvalution(Map map) {
		// TODO Auto-generated method stub
		return areaEvaluationMapper.getCityEvalution(map);
	}

	@Override
	public Map getCityCountIndex(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return areaEvaluationMapper.getCityCountIndex(map);
	}

	@Override
	public int getCityEvalutionAllPage(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return areaEvaluationMapper.getCityEvalutionAllPage(map);
	}

	@Override
	public List<Map<String, String>> getCityEvalutionAll(Map map) {
		// TODO Auto-generated method stub
		return areaEvaluationMapper.getCityEvalutionAll(map);
	}
	
	

}
