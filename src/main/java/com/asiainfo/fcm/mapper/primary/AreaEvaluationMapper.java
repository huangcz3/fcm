package com.asiainfo.fcm.mapper.primary;

import java.util.List;
import java.util.Map;

public interface AreaEvaluationMapper{

	
	/***
	 * 查询地市统计指标
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
