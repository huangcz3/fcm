package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IEvalutionReportService;
import com.asiainfo.fcm.util.ResultUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evalution")
public class EvalutionReportController extends BaseController {
	
	private static Logger logger = Logger.getLogger(EvalutionReportController.class);
	
	@Autowired
	private IEvalutionReportService evalutionReportService;
	
	/**
	 *  获取效果评估 综合评估信息 
	 *  @return List<Map> 集合
	 * */
	@GetMapping("/getEvaluationReportInfo")
    public Result getEvaluationReportInfo(HttpServletRequest request)throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String date = request.getParameter("date").toString();
		String isFiltered = request.getParameter("filtered").toString();
		Map parameterMap = new HashMap<String, Object>();
		parameterMap.put("date", date);
		parameterMap.put("isFiltered", isFiltered);
		if (date != "" && !date.isEmpty()) {
			//获取当月累计指标数据
			Map<String, Object> indicatorDataMap = evalutionReportService.getIndicatorDataByMonth(parameterMap);
			//活动top3
			List<Map<String, Object>> actTopMap = evalutionReportService.getActivityTopByMonth(parameterMap);
			//渠道top3
			//Map<String,Object> channelTopMap = evalutionReportService.getChannelTopByMonth(parameterMap);
			//优秀案例top5
			List<Map<String, Object>> caseMap = evalutionReportService.getExcellentCase(parameterMap);

			resultMap.put("targetNum", indicatorDataMap);//当月累计指标
			resultMap.put("top3Activity", actTopMap);//当月累计指标
			resultMap.put("goodJobs", caseMap);//当月累计指标

			return ResultUtil.success(resultMap);

		} else {
			logger.error(this.getClass().getMethods() + ":时间参数为空！");
			return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
		}

	}
}
