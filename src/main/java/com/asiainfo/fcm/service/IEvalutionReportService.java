package com.asiainfo.fcm.service;

import java.util.List;
import java.util.Map;

public interface IEvalutionReportService {

	/**
	 * 
	 * 当月累计指标数据
	 * */
	Map<String,Object> getIndicatorDataByMonth(Map map);
	
	/**
	 * 获取累计活动TOP3
	 * */
	List<Map<String,Object>> getActivityTopByMonth(Map map);
	
	/**
	 * 获取优秀案例(营销成功率TOP5)
	 * */
	List<Map<String, Object>> getExcellentCase(Map map);
	
	/**
	 * 获取渠道分发TOP3
	 * */
	Map<String,Object> getChannelTopByMonth(Map map);
	
}
