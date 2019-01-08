package com.asiainfo.fcm.service;

import java.util.List;
import java.util.Map;

/**
 * Created by LiuJH on 2018/4/28.
 */
public interface IReportService {
    List<List<String>> getReportByOpTime(String opTime);
}
