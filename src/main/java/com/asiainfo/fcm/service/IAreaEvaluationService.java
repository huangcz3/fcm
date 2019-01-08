package com.asiainfo.fcm.service;

import java.util.List;
import java.util.Map;

public interface IAreaEvaluationService {
	
	
	/***
	 * 查询地市列表数据
	 */
	List<Map<String,Object>> getCityEvalution(Map map);
	
	/***
	 * 查询地市统计指标值
	 */
	Map getCityCountIndex(Map<String, Object> map);
	
	int getCityEvalutionAllPage(Map<String, Object> map);
	
	/***
	 * 导出数据
	 */
	List<Map<String,String>> getCityEvalutionAll(Map map);
}
