package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.mapper.primary.BaseInitMapper;
import com.asiainfo.fcm.model.AdPosition;
import com.asiainfo.fcm.model.IopInfo;
import com.asiainfo.fcm.service.IBaseInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class BaseServiceImpl implements IBaseInitService {

	@Autowired
	private BaseInitMapper initMapper;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public List<SceneInfo> getSceneInit(String cityId) {
		// TODO Auto-generated method stub
		return initMapper.getScene(cityId);
	}

	@Override
	public List<ChannelInfo> getChanelInitByCityId(String cityId) {
		List<ChannelInfo> channelInfoList = initMapper.getChanelInitByCityId(cityId);
		/*for(int i=0;i<channelInfoList.size();i++){
			List<ApproverInfo> approverInfoList = initMapper.getApproverInfo(channelInfoList.get(i).getChannelId(),cityId);
			if (channelInfoList.get(i).getNeedChannelApproval() == 1){
				List<ApproverInfo> approverInfos = initMapper.getChannelApproverInfo(channelInfoList.get(i).getChannelId(),2);
				for(ApproverInfo approver :approverInfos){
					approverInfoList.add(approver);
				}
			}
			channelInfoList.get(i).setApproverInfoList(approverInfoList);
		}*/
		for(int i=0;i<channelInfoList.size();i++) {
			String channelId = channelInfoList.get(i).getChannelId();
			List<ApproverInfo> approverInfoList = initMapper.getApproverInfo(channelId,cityId);
			channelInfoList.get(i).setApproverInfoList(approverInfoList);
		}

		return channelInfoList;
	}

	@Override
	public List<Map<String, Object>> getReconmondList(Map<String, Object> map) {
		return initMapper.getReconmondList(map);
	}

	@Override
	public int getReconmondListAllTotal(Map<String, Object> map) {
		return initMapper.getReconmondListAllTotal(map);
	}

	@Override
	public List<Map<String, String>> getReconmondSecondList(Map<String, Object> map) {
		return initMapper.getReconmondSecondList(map);
	}

	@Override
	public int getReconmondSecondListTotal(Map<String, Object> map) {
		return initMapper.getReconmondSecondListTotal(map);
	}

	@Override
	public List<MarketingPurpose> getMarketingPurposes() {
		return initMapper.getMarketingPurposes();
	}

	@Override
	public List<BusinessType> getBusinessList() {
		return initMapper.getBusinessList();
	}

	@Override
	public List<City> getCityList(String cityId) {
		return initMapper.getCityList(cityId);
	}

	@Override
	public List<AdPosition> getAdPosition() {
		return initMapper.getAdPosition();
	}

	@Override
	public List<Map> getLinkCode() {
		return initMapper.getLinkCode();
	}

	@Override
	public List<IopInfo> getIopDropInfo() {
		return initMapper.getIopDropInfo();
	}

	@Override
	public List<Map> getPayType() {
		return initMapper.getPayType();
	}

	@Override
	public List<Map> getRearEndType() {
		return initMapper.getRearEndType();
	}

	@Override
	public List<AppTypeDefine> getAppTypeInfo() {
		return initMapper.getAppTypeInfo();
	}

	@Override
	public List<Map> getAppInfoList(Map paramMap) {
		return initMapper.getAppInfoList(paramMap);
	}

	@Override
	public List<ChannelInfo> getChanelsBySceneId(Map map) {
		List<ChannelInfo> channelInfoList = initMapper.getChanelsBySceneId(map.get("sceneId").toString());
		for(int i=0;i<channelInfoList.size();i++){
			List<ApproverInfo> approverInfoList = initMapper.getApproverInfo(channelInfoList.get(i).getChannelId(),map.get("cityId").toString());
			/*if (channelInfoList.get(i).getNeedChannelApproval() == 1){
				List<ApproverInfo> approverInfos = initMapper.getChannelApproverInfo(channelInfoList.get(i).getChannelId(),2);
				for(ApproverInfo approver :approverInfos){
					approverInfoList.add(approver);
				}
			}*/
			channelInfoList.get(i).setApproverInfoList(approverInfoList);
		}
		return channelInfoList;
	}

	@Override
	public List<Map> getEvaCityList() {
		return initMapper.getEvaCityList();
	}

	@Override
	public List<Map> getChannelList() {
		return initMapper.getChannelList();
	}

	@Override
	public List<Map> getIndexList() {
		return initMapper.getIndexList();
	}

	@Override
	public String getSceneSmsTemplate(String sceneId) {
		return initMapper.getSceneSmsTemplate(sceneId);
	}

	@Override
	public List<Map> getTop15Product(String cityId){
		return initMapper.getTop15Product(cityId);
	}

	@Override
	public String getPN(String PN, String curUUID){
		if ( curUUID == "" ){
			String newUUID = UUID.randomUUID().toString();
			stringRedisTemplate.opsForValue().set(newUUID,PN,60*2, TimeUnit.SECONDS);
			return  newUUID.replaceAll("-","");
		}else{
			Long isExpired = stringRedisTemplate.getExpire(curUUID);
			if ( isExpired != -2 ){
				return curUUID.replaceAll("-","");
			}else{
				String newUUID = UUID.randomUUID().toString();
				stringRedisTemplate.opsForValue().set(newUUID,PN,60, TimeUnit.SECONDS);
				return newUUID.replaceAll("-","");
			}
		}
	}

	@Override
	public List<Map> getRealTimePositionType() {
		return initMapper.getRealTimePositionType();
	}

	@Override
	public List<Map> getRealTimePositionInfos(Map map) {
		return initMapper.getRealTimePositionInfos(map);
	}

	@Override
	public List<String> getAllLevel() {
		return initMapper.getAllLevel();
	}

	@Override
	public List<Map<String, Object>> getProductTop20(Map map) {
		return initMapper.getProductTop20(map);
	}

	@Override
	public List<Map> getCustomerList(Map map) {
		return initMapper.getCustomerList(map);
	}
}
