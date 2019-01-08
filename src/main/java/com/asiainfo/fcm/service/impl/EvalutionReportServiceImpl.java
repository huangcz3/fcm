package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.primary.EvalutionReportMapper;
import com.asiainfo.fcm.service.IEvalutionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EvalutionReportServiceImpl implements IEvalutionReportService {

	@Autowired
	EvalutionReportMapper evalutionReportMapper;

	@Override
	public Map<String, Object> getIndicatorDataByMonth(Map map) {
		// TODO Auto-generated method stub
		return evalutionReportMapper.getIndicatorDataByMonth(map);
	}

	@Override
	public List<Map<String, Object>> getActivityTopByMonth(Map map) {
		// TODO Auto-generated method stub
		return evalutionReportMapper.getActivityTopByMonth(map);
	}

	@Override
	public List<Map<String, Object>> getExcellentCase(Map map) {
		// TODO Auto-generated method stub
		return evalutionReportMapper.getExcellentCase(map);
	}

	@Override
	public Map<String, Object> getChannelTopByMonth(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

}
