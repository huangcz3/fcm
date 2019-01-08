package com.asiainfo.fcm.mapper.primary;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by LiuJH on 2018/4/28.
 */
public interface RedSeaReportMapper {

    List<Map> getReportByOpTime(@Param("opTime") String opTime,@Param("tablename") String tablename);
}
