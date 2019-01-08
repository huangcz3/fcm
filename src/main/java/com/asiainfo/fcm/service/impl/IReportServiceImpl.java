package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.primary.RedSeaReportMapper;
import com.asiainfo.fcm.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuJH on 2018/4/28.
 */
@Service
public class IReportServiceImpl implements IReportService {

    @Autowired
    private RedSeaReportMapper redSeaReportMapper;

    private final String TABLE_NAME_PREFIX = "redsea_report_";

    @Override
    public List<List<String>> getReportByOpTime(String opTime) {

        List<Map> mapList = redSeaReportMapper.getReportByOpTime(opTime, TABLE_NAME_PREFIX + opTime.substring(0,6));

        List<List<String>> result = new ArrayList<>();
        mapList.forEach(m -> {
            List<String> list = new ArrayList<>();
            boolean b;
            b = m.get("PORT_ID") != null ? list.add((String) m.get("PORT_ID")) : list.add("-");
            b = m.get("PORT_NAME") != null ? list.add((String) m.get("PORT_NAME")) : list.add("-");
            b = m.get("GMBY_BXL_TARGET_NUM") != null ? list.add( m.get("GMBY_BXL_TARGET_NUM").toString()) : list.add("-");
            b = m.get("GMBY_BXL_DAY_NUM") != null ? list.add( m.get("GMBY_BXL_DAY_NUM").toString()) : list.add("-");
            b = m.get("GMBY_BXL_TOTALNUM") != null ? list.add( m.get("GMBY_BXL_TOTALNUM").toString()) : list.add("-");
            b = m.get("GMBY_BXL_TARGET_RATE") != null ? list.add( m.get("GMBY_BXL_TARGET_RATE").toString()) : list.add("-");
            b = m.get("GMBY_BXL_DAY_ACHIEVE") != null ? list.add( m.get("GMBY_BXL_DAY_ACHIEVE").toString()) : list.add("-");
            b = m.get("GMBY_YDQZ_TARGET_NUM") != null ? list.add( m.get("GMBY_YDQZ_TARGET_NUM").toString()) : list.add("-");
            b = m.get("GMBY_YDQZ_DAY_NUM") != null ? list.add( m.get("GMBY_YDQZ_DAY_NUM").toString()) : list.add("-");
            b = m.get("GMBY_YDQZ_TOTALNUM") != null ? list.add( m.get("GMBY_YDQZ_TOTALNUM").toString()) : list.add("-");
            b = m.get("GMBY_YDQZ_TARGET_RATE") != null ? list.add( m.get("GMBY_YDQZ_TARGET_RATE").toString()) : list.add("-");
            b = m.get("GMBY_YDQZ_DAY_ACHIEVE") != null ? list.add( m.get("GMBY_YDQZ_DAY_ACHIEVE").toString()) : list.add("-");
            b = m.get("GMBY_KDDSQZ_TARGET_NUM") != null ? list.add( m.get("GMBY_KDDSQZ_TARGET_NUM").toString()) : list.add("-");
            b = m.get("GMBY_KDDSQZ_DAY_NUM") != null ? list.add( m.get("GMBY_KDDSQZ_DAY_NUM").toString()) : list.add("-");
            b = m.get("GMBY_KDDSQZ_TOTALNUM") != null ? list.add( m.get("GMBY_KDDSQZ_TOTALNUM").toString()) : list.add("-");
            b = m.get("GMBY_KDDSQZ_TARGET_RATE") != null ? list.add( m.get("GMBY_KDDSQZ_TARGET_RATE").toString()) : list.add("-");
            b = m.get("GMBY_KDDSQZ_DAY_ACHIEVE") != null ? list.add( m.get("GMBY_KDDSQZ_DAY_ACHIEVE").toString()) : list.add("-");
            b = m.get("JZTS_LLJZB_TARGET_NUM") != null ? list.add( m.get("JZTS_LLJZB_TARGET_NUM").toString()) : list.add("-");
            b = m.get("JZTS_LLJZB_DAY_NUM") != null ? list.add( m.get("JZTS_LLJZB_DAY_NUM").toString()) : list.add("-");
            b = m.get("JZTS_LLJZB_TOTALNUM") != null ? list.add( m.get("JZTS_LLJZB_TOTALNUM").toString()) : list.add("-");
            b = m.get("JZTS_LLJZB_TARGET_RATE") != null ? list.add( m.get("JZTS_LLJZB_TARGET_RATE").toString()) : list.add("-");
            b = m.get("JZTS_LLJZB_DAY_ACHIEVE") != null ? list.add( m.get("JZTS_LLJZB_DAY_ACHIEVE").toString()) : list.add("-");
            b = m.get("JZTS_DSB_TARGET_NUM") != null ? list.add( m.get("JZTS_DSB_TARGET_NUM").toString()) : list.add("-");
            b = m.get("JZTS_DSB_DAY_NUM") != null ? list.add( m.get("JZTS_DSB_DAY_NUM").toString()) : list.add("-");
            b = m.get("JZTS_DSB_TOTALNUM") != null ? list.add( m.get("JZTS_DSB_TOTALNUM").toString()) : list.add("-");
            b = m.get("JZTS_DSB_TARGET_RATE") != null ? list.add( m.get("JZTS_DSB_TARGET_RATE").toString()) : list.add("-");
            b = m.get("JZTS_DSB_DAY_ACHIEVE") != null ? list.add( m.get("JZTS_DSB_DAY_ACHIEVE").toString()) : list.add("-");
            b = m.get("JZTS_BXLJSB_TARGET_NUM") != null ? list.add( m.get("JZTS_BXLJSB_TARGET_NUM").toString()) : list.add("-");
            b = m.get("JZTS_BXLJSB_DAY_NUM") != null ? list.add( m.get("JZTS_BXLJSB_DAY_NUM").toString()) : list.add("-");
            b = m.get("JZTS_BXLJSB_TOTALNUM") != null ? list.add( m.get("JZTS_BXLJSB_TOTALNUM").toString()) : list.add("-");
            b = m.get("JZTS_BXLJSB_TARGET_RATE") != null ? list.add( m.get("JZTS_BXLJSB_TARGET_RATE").toString()) : list.add("-");
            b = m.get("JZTS_BXLJSB_DAY_ACHIEVE") != null ? list.add( m.get("JZTS_BXLJSB_DAY_ACHIEVE").toString()) : list.add("-");
            result.add(list);
        });
        return result;
    }
}
