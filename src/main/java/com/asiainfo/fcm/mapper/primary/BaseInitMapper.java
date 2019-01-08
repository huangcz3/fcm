package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.model.AdPosition;
import com.asiainfo.fcm.model.IopInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseInitMapper {

	List<SceneInfo> getScene(String cityId);

	List<ChannelInfo> getChanelInitByCityId(String cityId);

	List<ApproverInfo> getApproverInfo(@Param("channelId") String channelId,@Param("cityId") String cityId);

	List<ApproverInfo> getChannelApproverInfo(@Param("channelId") String channelId,@Param("level") int level);

	List<Map<String,Object>> getReconmondList(Map<String,Object> map);

	int getReconmondListAllTotal(Map<String,Object> map);

	List<Map<String,String>> getReconmondSecondList(Map<String,Object> map);

	int getReconmondSecondListTotal(Map<String,Object> map);

	List<MarketingPurpose> getMarketingPurposes();

	List<BusinessType> getBusinessList();

	List<City> getCityList(@Param("cityId") String cityId);

	List<AdPosition> getAdPosition();

	List<Map> getLinkCode();

	List<IopInfo> getIopDropInfo();

	List<Map> getPayType();

	List<Map> getRearEndType();

	List<AppTypeDefine> getAppTypeInfo();

	List<Map> getAppInfoList(Map paramMap);

	List<ChannelInfo> getChanelsBySceneId(String sceneId);

	List<Map> getEvaCityList();

	List<Map> getChannelList();

	List<Map> getIndexList();

	String getSceneSmsTemplate(String sceneId);

	List<Map> getTop15Product(String cityId);

	List<Map> getRealTimePositionType();

	List<Map> getRealTimePositionInfos(Map map);

    List<String> getAllLevel();

    List<Map<String,Object>> getProductTop20(Map map);

	List<Map> getCustomerList(Map map);
}
