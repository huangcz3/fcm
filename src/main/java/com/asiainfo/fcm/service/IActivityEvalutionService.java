package com.asiainfo.fcm.service;

import java.util.List;
import java.util.Map;

public interface IActivityEvalutionService {

	List<Map<String,Object>> getActEvalution(Map map);
	
	List<Map> getActEvaDetail(Map<String, Object> map);
	
	//获取总条数
	int getActEvalutionTotals(Map map);
	
	//数据下载
	List<Map<String,String>> getActEvalutionAll(Map map);

	//活动评估图标详细
	Map getActEvalutionDetail(String activityId,String indexs);
}
