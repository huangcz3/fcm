package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.ActivityOpRecordInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chengzhongtao on 2017/12/15.
 */
@Repository
public interface ActivityOpRecordInfoMapper {
    /**
     * 查询所有操作
     */
    List<ActivityOpRecordInfo> getAllSmsOperate(@Param("activityId") String activityId);

    void saveOperateInfo(ActivityOpRecordInfo aop);
}
