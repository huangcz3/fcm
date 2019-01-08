package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.City;
import com.asiainfo.fcm.entity.Quota;
import com.asiainfo.fcm.mapper.primary.QuotaMapper;
import com.asiainfo.fcm.service.IQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/28.
 */
@Service
@Transactional
public class QuotaServiceImpl implements IQuotaService{

    @Autowired
    private QuotaMapper quotaMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Map<String, Object>> getChannels(){
        return quotaMapper.getChannels();
    }

    @Override
    public List<Quota> getQuotaList(Map paramaterMap){
        String channelId = paramaterMap.get("channelId").toString();
        List<Quota> quotaList = quotaMapper.getQuotaList(paramaterMap);
        if( channelId.equals("d05") ){
            String redisKey  = "SENDED_CNT_d05_"+paramaterMap.get("effectTime").toString().replaceAll("-","");
            Boolean isExist = stringRedisTemplate.hasKey(redisKey);
            Map cityCounts = stringRedisTemplate.opsForHash().entries(redisKey);

            quotaList.stream().forEach(quota -> {
                int sendLimit = isExist?Integer.parseInt(cityCounts.get(quota.getCityId().trim()).toString()):0;
                quota.setSendedCount(sendLimit);
            });
        }
        return quotaList;
    }
    @Override
    public  int getQuotaListTotalRecords(Map paramaterMap){
        return quotaMapper.getQuotaListTotalRecords(paramaterMap);
    }

    @Override
    public void setCityQuota(Quota parameterMap){
        quotaMapper.setCityQuota(parameterMap);
    }

    @Override
    public void addCityQuota(Quota parameterMap){
        quotaMapper.addCityQuota(parameterMap);
    }

    @Override
    public List<City> getCityList(String channelId){
       return  quotaMapper.getCityList(channelId);
    }

    @Override
    public Quota checkQuotaIsExist(Quota parameterMap){
        return  quotaMapper.checkQuotaIsExist(parameterMap);
    }
}
