package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.City;
import com.asiainfo.fcm.entity.Quota;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/28.
 */
public interface IQuotaService {
    List<Map<String, Object>> getChannels();
    List<Quota> getQuotaList(Map paramaterMap);
    int getQuotaListTotalRecords(Map paramaterMap);
    void setCityQuota(Quota parameterMap);
    void addCityQuota(Quota parameterMap);
    List<City> getCityList(String channelId);
    Quota checkQuotaIsExist(Quota parameterMap);
}
