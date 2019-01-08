package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.model.AdPosition;
import com.asiainfo.fcm.model.IopInfo;

import java.util.List;
import java.util.Map;

public interface IBaseInitService {

	//获取场景选项
	List<SceneInfo> getSceneInit(String cityId);

	//获取渠道信息
	List<ChannelInfo> getChanelInitByCityId(String cityId);

	//获取推荐业务
	List<Map<String,Object>> getReconmondList(Map<String,Object> map);
	int getReconmondListAllTotal(Map<String,Object> map);
	List<Map<String,String>> getReconmondSecondList(Map<String,Object> map);
	int getReconmondSecondListTotal(Map<String,Object> map);

	//获取营销目的
	List<MarketingPurpose> getMarketingPurposes();

	//获取营销类型
	List<BusinessType> getBusinessList();

	//获取地市列表
	List<City> getCityList(String cityId);

	//获取掌厅 广告位下拉
	List<AdPosition> getAdPosition();

	//获取掌厅 链接码表
	List<Map> getLinkCode();

	//获取互联网渠道
	List<IopInfo> getIopDropInfo();

	//获取缴费渠道
	List<Map> getPayType();

	//获取追尾短信提醒类型
	List<Map> getRearEndType();

	//获取上网行为分类行为
	List<AppTypeDefine> getAppTypeInfo();

	//根据app类型获取app信息
	List<Map> getAppInfoList(Map map);

	//根据所选场景获取可执行渠道
	List<ChannelInfo> getChanelsBySceneId(Map map);

	//获取地市
	List<Map> getEvaCityList();

	//获取渠道
	List<Map> getChannelList();

	//获取展示指标下拉选项
	List<Map> getIndexList();

	//获取场景短息模板
	String getSceneSmsTemplate(String sceneId);

	//获取业务订购top15
	List<Map> getTop15Product(String cityId);

	//获取PN值
	String getPN(String PN,String curUUID);

	//获取实时位置类型下拉
	List<Map> getRealTimePositionType();

	//获取实时位置详细信息
	List<Map> getRealTimePositionInfos(Map map);

	// 查询所有的审批层级
    List<String> getAllLevel();

    // 查询常用资费代码top20
    List<Map<String,Object>> getProductTop20(Map map);

	//查询地市选取客户群来源
	List<Map> getCustomerList(Map map);
}
