package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.mapper.primary.IOPActivityMapper;
import com.asiainfo.fcm.service.IIOPActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 */
@Service
@Transactional
public class IOPActivityServiceImpl implements IIOPActivityService{

    @Autowired
    private IOPActivityMapper iopActivityMapper;

    @Override
    public List<IOPActivityInfo> getActivityList(Map parameterMap){
        return iopActivityMapper.getActivityList(parameterMap);
    }

    @Override
    public int getActivityListTotals(Map parameterMap){
        return iopActivityMapper.getActivityListTotals(parameterMap);
    }

    @Override
    public String getUserAuthority(String userPhoneNo){
        String result = iopActivityMapper.getUserAuthority(userPhoneNo);
        String permition = result == null?"-1":result;
        return permition;
    }

    @Override
    public IOPActivityInfo getPublicAttrInfo(String activityId){
        return iopActivityMapper.getPublicAttrInfo(activityId);
    }

    @Override
    public List<IOPCampaginInfo> getCampaginInfo(String activityId){
        return iopActivityMapper.getCampaginInfo(activityId);
    }

    @Override
    public Map getBelongerInfo(Map parameterMap){
        return iopActivityMapper.getBelongerInfo(parameterMap);
    }

    @Override
    public List<IOPUserInfo> getAllUserList(){
        return iopActivityMapper.getAllUserList();
    }

    @Override
    public IopSendOrder checkSendOrderInfo(IopSendOrder parameter){
         return iopActivityMapper.checkSendOrderInfo(parameter);
    }

    @Override
    public void insertIntoG2PActDownRel(IopSendOrder parameter){
        iopActivityMapper.insertIntoG2PActDownRel(parameter);
    }

    @Override
    public void insertIntoCampaginAndCampRel(List<IOPRelateCampagin> parameterList){
        String campaignId = parameterList.get(0).getCampaignId();
        iopActivityMapper.deleteCampsByCampaignId(campaignId);

        for (int i=0;i<parameterList.size();i++){
            iopActivityMapper.insertIntoCampaginAndCampRel(parameterList.get(i));
        }
    }

    @Override
    public List<IOPRelateCampagin> getRelateCampaginAndCamp(String campaignId){
        return iopActivityMapper.getRelateCampaginAndCamp(campaignId);
    }
}
