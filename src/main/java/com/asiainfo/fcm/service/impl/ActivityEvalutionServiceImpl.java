package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.primary.ActivityEvalutionMapper;
import com.asiainfo.fcm.service.IActivityEvalutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityEvalutionServiceImpl implements IActivityEvalutionService {

	@Autowired
	ActivityEvalutionMapper activityEvalutionMapper;


	@Override
	public List<Map<String,Object>> getActEvalution(Map paramMap) {
		
		List<Map<String,Object>> actList = activityEvalutionMapper.getActEvalution(paramMap);
		List<String> idList = new ArrayList<String>();
		if(actList.size()>0 && !actList.isEmpty()){
			for(int i=0;i<actList.size();i++){
				String id = actList.get(i).get("CAMP_ID").toString();
				idList.add(id);
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("campIdList", idList);
			
			List<Map> detailMap = activityEvalutionMapper.getActEvaDetail(map);
			for(int i=0;i<actList.size();i++){
				String campId = actList.get(i).get("CAMP_ID").toString();
				String channelName= actList.get(i).get("CHANNEL_NAME").toString();
				Map<String,Object> touchMap = new HashMap<String,Object>();
				
				Map<String,Object> vicMap = new HashMap<String,Object>();
				
				int touchTotal = 0;
				int vicTotal = 0;
				
				List<Map<String,Object>> touchdetail  = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> vicdetail  = new ArrayList<Map<String,Object>>();
				for(int j =0;j<detailMap.size();j++){
					if(campId.equals(detailMap.get(j).get("CAMP_ID"))){
						Map<String,Object> touchItem = new HashMap<String,Object>();
						Map<String,Object> vicItem = new HashMap<String,Object>();
						touchTotal += Integer.parseInt(detailMap.get(j).get("TOUCH_NUM").toString());
						vicTotal += Integer.parseInt(detailMap.get(j).get("VIC_NUM").toString());
						
						String tempChannel = detailMap.get(j).get("CHANNEL_NAME").toString();
						if(!channelName.contains(tempChannel)){
							channelName = channelName +" ，"+tempChannel;
							actList.get(i).put("CHANNEL_NAME", channelName);
						}
						
						touchItem.put("NAME", detailMap.get(j).get("CHANNEL_NAME"));
						touchItem.put("VALUE", detailMap.get(j).get("TOUCH_NUM"));
						touchdetail.add(touchItem);
						
						vicItem.put("NAME", detailMap.get(j).get("CHANNEL_NAME"));
						vicItem.put("VALUE", detailMap.get(j).get("VIC_NUM").toString());
						vicdetail.add(vicItem);
					}
				}
				touchMap.put("ALLTOTAL", touchTotal);
				touchMap.put("TOUCHLIST", touchdetail);
				vicMap.put("ALLTOTAL", vicTotal);
				vicMap.put("VICLIST", vicdetail);
				
				actList.get(i).put("TOUCH_NUM", touchMap);
				actList.get(i).put("VIC_NUM", vicMap);
				
			}
		}else{
			actList = null;
		}
		
		return actList;
	}
	@Override
	public List<Map> getActEvaDetail(Map<String,Object> map) {
		return activityEvalutionMapper.getActEvaDetail(map);
	}
	@Override
	public int getActEvalutionTotals(Map map) {
		// TODO Auto-generated method stub
		return activityEvalutionMapper.getActEvalutionTotals(map);
	}
	@Override
	public List<Map<String, String>> getActEvalutionAll(Map map) {
		// TODO Auto-generated method stub
		return activityEvalutionMapper.getActEvalutionAll(map);
	}

	@Override
	public Map getActEvalutionDetail(String activityId, String indexs) {
		String[] item = indexs.split(",");
		Map paramMap = new HashMap<>();
		String params = "";
		for(String s : item){
			params += s.substring(s.indexOf("-")+1,s.length())+",";
			paramMap.put(s.substring(s.indexOf("-")+1,s.length()),s.substring(0,s.indexOf("-")));
		}
		params = params.substring(0,params.length()-1);
		List<Map> infoList =  activityEvalutionMapper.getActEvalutionDetail(activityId,params);


		Map resultMap = new HashMap<>();
		List<String> dateList = new ArrayList<>();

		for(int i=0;i<infoList.size();i++){
			dateList.add(infoList.get(i).get("CUR_DATE").toString());


		}
		List<Map> dataInfo = new ArrayList<>();
		String[] keyList = params.split(",");
		for(int j=0;j<keyList.length;j++){
			Map info = new HashMap<>();
			info.put("name",paramMap.get(keyList[j]));

			List<String> dataList = new ArrayList<>();
			for(int i=0;i<infoList.size();i++){
				dataList.add(infoList.get(i).get(keyList[j]).toString());
			}
			info.put("info",dataList);
			dataInfo.add(info);

		}
		resultMap.put("data_info",dataInfo);
		resultMap.put("date_info",dateList);

		return resultMap;
	}


}
