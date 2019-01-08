package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.service.IReportService;
import com.asiainfo.fcm.util.DateUtil;
import com.asiainfo.fcm.util.ExcelExportUtil;
import com.asiainfo.fcm.util.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 红海行动
 * Created by RUOK on 2017/6/16.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private IReportService reportService;

    @GetMapping("/getReport")
    public Result getReportByOpTime(String opTime) {
        if (StringUtils.isBlank(opTime)) {
            opTime = DateUtil.getCurrentYYYYMMDDString();
        }
        List<List<String>> list = reportService.getReportByOpTime(opTime);
        return ResultUtil.success(list);
    }

    @GetMapping("/downloadReport")
    public void downloadReportByOpTime(HttpServletResponse response, String opTime) {
        try {
            if (StringUtils.isBlank(opTime)) {
                opTime = DateUtil.getCurrentYYYYMMDDString();
            }
            List<List<String>> list = reportService.getReportByOpTime(opTime);
            if (list.size() == 0) {
                response.getWriter().println("no data");
                return;
            }
            String fileName = "红海行动_" + opTime + ".xls";
            logger.info("下载文件名：{}", fileName);
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();

            ExcelExportUtil excelExportUtil = ExcelExportUtil.getInstance().readTemplateByClasspath("static/template/template.xls");
            for (int j = 0; j < list.size(); j++) {
                excelExportUtil.createNewRow();
                for (int i = 0; i < 32; i++) {
                    excelExportUtil.createCell(list.get(j).get(i));
                }
            }
            Map<String, String> datas = new HashMap<String, String>();
            datas.put("title", "红海行动各中心完成情况日报_" + opTime);
            excelExportUtil.replaceFinalData(datas);
            excelExportUtil.clearDefaultStyle();
            excelExportUtil.writeToStream(outputStream);
        } catch (Exception e) {
            logger.error("红海行动下载文件异常", e);
        }
    }
}
