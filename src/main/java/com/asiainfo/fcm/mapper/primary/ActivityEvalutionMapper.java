package com.asiainfo.fcm.mapper.primary;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityEvalutionMapper {

	List<Map<String,Object>> getActEvalution(Map map);
	
	List<Map> getActEvaDetail(Map<String, Object> map);
	
	//获取总条数
	int getActEvalutionTotals(Map map);
	
	//数据下载
	List<Map<String,String>> getActEvalutionAll(Map map);

	List<Map> getActEvalutionDetail(@Param("activityId") String activityId, @Param("indexs") String indexs);
}
