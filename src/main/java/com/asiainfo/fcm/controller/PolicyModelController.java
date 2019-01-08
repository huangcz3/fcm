package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.PolicyModelActivity;
import com.asiainfo.fcm.entity.PolicySceneActivity;
import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IPolicyModelService;
import com.asiainfo.fcm.util.ExcelUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
@RestController
@RequestMapping("/policyModel")
public class PolicyModelController {
    @Autowired
    private IPolicyModelService policyModelService;

    @GetMapping("/getPolicyModelList")
    public Result getPolicyModelList(HttpSession session, @RequestParam("type") String type,@RequestParam("modelType") String modelType, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("searchVal") String searchVal, @RequestParam("pageSize") String pageSize, @RequestParam("page") String page) throws Exception{
        Map parameterMap = new HashMap();
        parameterMap.put("modelType",modelType);
        parameterMap.put("startTime",startTime);
        parameterMap.put("endTime",endTime);
        parameterMap.put("searchVal",searchVal);
        parameterMap.put("type",type);
        int lengthRow = Integer.parseInt(pageSize);
        int currentPage = Integer.parseInt(page);
        int startRow = (currentPage - 1)*lengthRow;
        parameterMap.put("lengthRow",lengthRow);
        parameterMap.put("startRow",startRow);

        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        parameterMap.put("userId",userId);

        Map result = new HashMap();
        List<PolicyModelActivity> caseActivityList = policyModelService.getPolicyModelList(parameterMap);
        int totals = policyModelService.getPolicyModelListTotals(parameterMap);
        result.put("activityList",caseActivityList);
        result.put("totals",totals);
        return ResultUtil.success(result);
    }


    @PostMapping("/saveNewPolicyModel")
    public Result saveNewPolicyModel(@RequestBody PolicyModelActivity policyModelActivity, HttpSession session) throws Exception{
        User user = UserUtil.getCurrentUser(session);
        String createId = user.getUserId();
        String createName = user.getUserName();
        policyModelActivity.setProId("280");
        policyModelActivity.setCreateId(createId);
        policyModelActivity.setCreateName(createName);
        policyModelService.savePolicyModelActivity(policyModelActivity);
        return ResultUtil.success();
    }

    @GetMapping("/getPolicyModelDetail")
    public Result getPolicyModelDetail(@RequestParam("modelId") String modelId) throws Exception{
        PolicyModelActivity activityDetail = policyModelService.getPolicyModelDetail(modelId);
        return ResultUtil.success(activityDetail);
    }

    @PostMapping("/uploadPolicyModelActivity")
    public Result uploadPolicyModelActivity(@RequestParam("modelId") String modelId,HttpSession session) throws Exception{
        Map paramMap = new HashMap();
        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userName = user.getUserName();

        paramMap.put("modelId",modelId);
        paramMap.put("userId",userId);
        paramMap.put("uploadName",userName);

        int isCanUp = policyModelService.isCanUp(paramMap);
        if (isCanUp > 0) {
            policyModelService.uploadActivity(paramMap);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    @PostMapping("/deleteModelActivity")
    public Result deleteModelActivity(@RequestParam("modelId") String modelId, HttpSession session) throws Exception{
        Map paramMap = new HashMap();
        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userName = user.getUserName();

        paramMap.put("modelId",modelId);
        paramMap.put("userId",userId);
        paramMap.put("uploadName",userName);

        int isCanUp = policyModelService.isCanUp(paramMap);
        if (isCanUp > 0) {
            policyModelService.deleteActivity(paramMap);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    @GetMapping("/getPolicyModelListDownload")
    public Result getPolicyModelListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        User user = UserUtil.getCurrentUser(session);

        Map resultMap = new HashMap<>();
        //获取参数
        String searchVal = request.getParameter("searchVal");
        String modelType = request.getParameter("modelType");
        String type = request.getParameter("type");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        Map parameterMap = new HashMap<>();

        parameterMap.put("searchVal", searchVal);
        parameterMap.put("modelType", modelType);
        parameterMap.put("type", type);
        parameterMap.put("startTime",startTime);
        parameterMap.put("endTime", endTime);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(new Date());
        String name = type.equals("1")? "四川_":"集团_";
        String sheetName = name+now;

        //获取列表数据
        List<Map<String,String>> list = policyModelService.getAllPolicyModelList(parameterMap);

        String [][] header = {{"模型名称","上报人","创建人","省份","上报日期","创建日期","状态","模型分类","更新时间","模型简述","模型目标","业务需求","需求提出方","目标用户量","结果输出方式","需求应用场景","建模工具",
                "硬件要求","软件运行环境","数据量","时间范围","来源","字段说明","变量列表","变量选取依据","缺失处理","数据标准化处理","异常值处理"," 其他处理",
                "输出数据格式","样本数据","核心算法","参数设定","模型描述","模型输出","模型工程文件","覆盖率","命中率","准确率","其它自定义指标","转化率","提升度","其它自定义指标"}};
        String columns = "modelName,uploadName,createName,proName,uploadDateTime,createTime,flag,modelType,updateTime,modelSimpleDesc,modelObjective,businessDesc,businessPerson,objectiveUserNum,resultType,caseSence,modelTool," +
                "handwareReq,softRun,dataNum,dateArrange,source,filedDesc,varList,varReason,defaultHandle,dataUniHandle,errorHandle,otherHandle," +
                "outputFormat,exampleData,coreMethod,paramPersume,modelDesc,modelOutput,modelFile,coverageRatio,goalRatio,accuracyRatio,otherSelfEva,transformRatio,increaseRatio,otherSelfKpi";
        exportComplexExcel(request,response,sheetName,header,columns,list);

        return ResultUtil.success();
    }

    public static <T> void exportComplexExcel(HttpServletRequest request,HttpServletResponse response, String sheetName, String[][] header,String columns, List<T> data) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msexecl");
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(sheetName.getBytes("gb2312"), "ISO8859-1") + ".xls");
        OutputStream os = response.getOutputStream();
        ExcelUtil excelImport = new ExcelUtil();
        excelImport.createBook(os);
        if (data != null && data.size() > 0 && !(data.get(0) instanceof Map))
            excelImport.writerSheetWithObject(sheetName, data,
                    columns.split(","), header);
        else
            excelImport.writerSheet(sheetName,
                    (List<Map<String, String>>) data, columns.toUpperCase()
                            .split(","), header);
        excelImport.closeBook();
        os.close();
    }
}
