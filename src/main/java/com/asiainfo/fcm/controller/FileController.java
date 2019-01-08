package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.ICustomerGroupService;
import com.asiainfo.fcm.service.IFileService;
import com.asiainfo.fcm.task.AsyncTask;
import com.asiainfo.fcm.util.FTPUtil;
import com.asiainfo.fcm.util.IDUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件上传及下载.
 * Created by RUOK on 2017/6/26.
 */
@Controller
@RequestMapping("/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private static final String base64Prefix = "data:image/";

    @Autowired
    private IFileService fileService;

    @Autowired
    private ICustomerGroupService customerGroupService;

    @Autowired
    private AsyncTask asyncTask;

    @Value("${fcm.file.upload.path}")
    private String uploadFilePath;

    @Value("${fcm.file.download.path}")
    private String downLoadFilePath;

    @Value("${fcm.customer-group.file-type}")
    private String[] customerGroupFileType;

    @Value("${fcm.image.file-type}")
    private String[] imageFileType;

    @Value("${fcm.image.max-size}")
    private long maxImgSize;

    @Value("${fcm.customer-group.tablename.prefix}")
    private String tableNamePreFix;

    @Value("${fcm.customer-group.tmp-tablename.prefix}")
    private String tmpTableNamePreFix;

    @Value("${fcm.customer-group.batch-insert.count}")
    private long batchInsertCount;

//    @Value("${fcm.customer-group.max-records}")
//    private long customerGroupMaxRecords;

    @Value("${fcm.customer-group.standard-template-name}")
    private String standardTemplateName;

    @Value("${fcm.customer-group.customize-template-name}")
    private String customizeTemplateName;

    @Value("${iop.host}")
    private String host;

    @Value("${iop.port}")
    private int port;

    @Value("${iop.username}")
    private String username;
    @Value("${iop.pass}")
    private String pass;

    @Value("${fcm.operation.help-doc-name}")
    private String helpDoc;

    @Value("${fcm.file.upload.path}")
    private String uploadPath;
    @Value("${iop.remotePath}")
    private String remote;

    @PostMapping("/upload")
    public void upload(HttpSession session, HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam(value = "useType", defaultValue = "0") int useType) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Result result = this.uploadFile(session, file, useType);
        objectMapper.writeValue(printWriter, result);
    }

    @GetMapping("/uploadTo228")
    public void uploadTo228(@RequestParam("fileName") String filename, @RequestParam(value = "activityId", required = true) String activityId) {

        String path = uploadPath;
        String fileNameOut = activityId + "_" + filename;
        String tmppath = path + File.separator + activityId + File.separator + fileNameOut;
        String remotePath = remote;
        File fileOut = new File(tmppath);
        File fileIn = new File(path + File.separator + filename);
        if (!fileOut.getParentFile().exists()) {
            fileOut.getParentFile().mkdirs();
        }
        try {
            fileOut.createNewFile();
            FileCopyUtils.copy(fileIn, fileOut);
            boolean flag = FTPUtil.uploadFile(host, port, username, pass, remotePath, fileNameOut, new FileInputStream(new File(tmppath)));
            if (flag) {
                logger.info("上传成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getUploadFile")
    @ResponseBody
    public Result getUploadFile(@RequestParam("fileName") String fileName) {
        UploadFile uploadFile = fileService.getUploadFile(fileName);
        return ResultUtil.success(uploadFile);
    }

    @GetMapping("/downLoadTemplate")
    public void downLoadTemplate(HttpServletResponse response, @RequestParam(name = "customizeFlag", defaultValue = "0") int customizeFlag) {
        try {
            response.setHeader("content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            //String fileName = customizeFlag == 1 ? customizeTemplateName : standardTemplateName;
            String fileName = "";
            if (customizeFlag == 0) {
                fileName = standardTemplateName;
            } else if (customizeFlag == 1) {
                fileName = customizeTemplateName;
            } else if (customizeFlag == 3) {
                fileName = helpDoc;
            }

            logger.info("模板名：{}", fileName);
            Path file = Paths.get(downLoadFilePath, fileName);

            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);

            Files.copy(file, response.getOutputStream());

        } catch (IOException e) {
            logger.error("下载模板文件异常", e);
        }
    }

    private Result uploadFile(HttpSession session, MultipartFile file, int useType) throws IOException {
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();

            if (originalFilename.contains("\\")) {
                originalFilename = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);
            }

            logger.info("处理上传文件：{}", originalFilename);

            if (originalFilename.contains(".")) {
                boolean isCustomerGroupFile = false;
                String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
                switch (useType) {
                    case 1:
                    case 2:
                    case 3:
                        if (!Arrays.asList(customerGroupFileType).contains(fileType.toLowerCase())) {
                            return ResultUtil.error(ResultEnum.CUSTOMER_GROUP_FILE_TYPE_NOT_MATCH);
                        }
                        isCustomerGroupFile = true;
                        break;
                    case 4:
                        if (!Arrays.asList(imageFileType).contains(fileType.toLowerCase())) {
                            return ResultUtil.error(ResultEnum.IMAGE_FILE_TYPE_NOT_MATCH);
                        } else if (file.getSize() > maxImgSize) {
                            return ResultUtil.error(ResultEnum.MAX_UPLOAD_SIZE_EXCEEDED);
                        }

                        break;
                    default:
                        return ResultUtil.error(ResultEnum.ILLEGAL_FILE_USE_TYPE);
                }
                String fileName = IDUtil.generateFileId() + fileType;
                File dest = new File(uploadFilePath + File.separator + fileName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);

                User user = UserUtil.getCurrentUser(session);

                UploadFile uploadFile = new UploadFile();
                uploadFile.setFileName(fileName);
                uploadFile.setOriginalFileName(originalFilename);
                uploadFile.setUploaderId(user.getUserId());
                uploadFile.setUploaderName(user.getUserName());
                uploadFile.setUseType(useType);

                if (!isCustomerGroupFile) {
                    String imgType = fileType.substring(1);
                    byte[] imgByte = Files.readAllBytes(dest.toPath());
                    String base64ImgString = base64Prefix + imgType + ";base64," + Base64.getEncoder().encodeToString(imgByte);

                    uploadFile.setImgBase64(base64ImgString);
                }

                fileService.saveUploadFile(uploadFile);

                if (isCustomerGroupFile) {
                    return this.createCustomerGroup(uploadFile, dest);
                } else {
                    return ResultUtil.success(fileName);
                }
            } else {
                return ResultUtil.error(ResultEnum.UNKNOWN_FILE_TYPE);
            }
        } else {
            return ResultUtil.error(ResultEnum.FILE_IS_BLANK);
        }
    }

    public Result createCustomerGroup(UploadFile uploadFile, File file) throws IOException {
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {


            List<CustomerGroupDetail> customerGroupDetailList = new ArrayList<>();

            long lineCount = 0;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (uploadFile.getUseType() == 2) {
                    if (isPhoneNoAndMarketing(line)) {
                        CustomerGroupDetail customerGroupDetail = new CustomerGroupDetail();

                        String phoneNo = line.substring(0, 11);
                        String marketing = line.substring(12);

                        customerGroupDetail.setPhoneNo(phoneNo);
                        customerGroupDetail.setMarketing(marketing);

                        customerGroupDetailList.add(customerGroupDetail);
                    }
                } else {
                    if (isPhoneNo(line)) {
                        CustomerGroupDetail customerGroupDetail = new CustomerGroupDetail();

                        customerGroupDetail.setPhoneNo(line);

                        customerGroupDetailList.add(customerGroupDetail);
                    }
                }

                ++lineCount;
            }

            if (customerGroupDetailList.size() == 0) {
                return ResultUtil.error(ResultEnum.NO_VALID_DATA);
//            } else if (customerGroupDetailList.size() > customerGroupMaxRecords) {
//                return ResultUtil.error(ResultEnum.MAX_RECORDS_EXCEEDED);
            } else {

                CustomerGroupInfo tmpCustomerGroupInfo = new CustomerGroupInfo();
                String tmpCustomerGroupId = IDUtil.generateCustomerGroupId();
                tmpCustomerGroupInfo.setGroupTableName(tmpTableNamePreFix + tmpCustomerGroupId);
                tmpCustomerGroupInfo.setCustomizeFlag(uploadFile.getUseType() == 2 ? 1 : 0);

                customerGroupService.createCustomerGroupTable(tmpCustomerGroupInfo);

                Map<String, Object> map = new HashMap<>();
                map.put("customerGroupInfo", tmpCustomerGroupInfo);

                int count = 0;
                int endIndex;
                do {
                    if (count + batchInsertCount > customerGroupDetailList.size()) {
                        endIndex = customerGroupDetailList.size();
                    } else {
                        endIndex = Long.valueOf(count + batchInsertCount).intValue();
                    }

                    List<CustomerGroupDetail> subCustomerGroupDetailList = customerGroupDetailList.subList(count, endIndex);
                    map.put("customerGroupDetailList", subCustomerGroupDetailList);

                    customerGroupService.saveCustomerGroupDetail(map);

                    count += batchInsertCount;
                } while (count < customerGroupDetailList.size());

                //剔重
                String customerGroupId = IDUtil.generateCustomerGroupId();

                CustomerGroupInfo customerGroupInfo = new CustomerGroupInfo();

                customerGroupInfo.setCustomerGroupId(customerGroupId);
                String customerGroupName = uploadFile.getOriginalFileName().substring(0, uploadFile.getOriginalFileName().lastIndexOf("."));
                customerGroupInfo.setCustomerGroupName(customerGroupName);
                customerGroupInfo.setGroupTableName(tableNamePreFix + customerGroupId);
                customerGroupInfo.setCreatorId(uploadFile.getUploaderId());
                customerGroupInfo.setCreatorName(uploadFile.getUploaderName());
                customerGroupInfo.setCreateType(3);
                customerGroupInfo.setCustomizeFlag(uploadFile.getUseType() == 2 ? 1 : 0);
                customerGroupInfo.setEffective(1);
                customerGroupInfo.setRemoveFlag(uploadFile.getUseType() == 3 ? 1 : 0);
                customerGroupInfo.setFileName(uploadFile.getFileName());
                customerGroupInfo.setOriginalFileName(uploadFile.getOriginalFileName());

                customerGroupService.createCustomerGroupTable(customerGroupInfo);

                map.put("distinctCustomerGroupInfo", customerGroupInfo);

                int amount = customerGroupService.distinctCustomerGroupDetail(map);

                customerGroupInfo.setAmount(amount);
                customerGroupInfo.setFileLineCount(lineCount);

                customerGroupService.saveCustomerGroupInfo(customerGroupInfo);

                //删除临时表
                customerGroupService.dropTmpTable(tmpCustomerGroupInfo.getGroupTableName());

                return ResultUtil.success(customerGroupInfo);
            }
        }
    }

    @PostMapping("/uploadChannelSuggest")
    public void uploadChannelSuggestFile(HttpSession session, HttpServletResponse response, @RequestParam("file") MultipartFile file) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Result result = null;

        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename.contains("\\")) {
                originalFilename = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);
            }
            logger.info("渠道建议处理上传文件：{}", originalFilename);
            String fileName = IDUtil.generateFileId() + "CS";
            File dest = new File(uploadFilePath + File.separator + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);

            User user = UserUtil.getCurrentUser(session);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("coustomerGroupId",IDUtil.generateCustomerGroupId());
            paramMap.put("createrId",user.getUserId());
            paramMap.put("createrName",user.getUserName());
            paramMap.put("createrPhone",user.getPhoneNo());
            paramMap.put("startTime",new Date());
            paramMap.put("originalFileName",originalFilename);
            paramMap.put("fileName",fileName);
            customerGroupService.saveChannelSuggestBaseInfo(paramMap);
            asyncTask.calculateChannelSuggestCustomer(fileName, (String) paramMap.get("coustomerGroupId"),user.getPhoneNo(),originalFilename);
            result = ResultUtil.success();
        } else {
            result = ResultUtil.error(ResultEnum.FILE_IS_BLANK);
        }

        objectMapper.writeValue(printWriter, result);
    }

    private static boolean isPhoneNo(String phoneNo) {
        String regExp = "^1\\d{10}$";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(phoneNo);
        return matcher.matches();
    }

    private static boolean isPhoneNoAndMarketing(String phoneNo) {
        String regExp = "^1\\d{10},.+";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(phoneNo);
        return matcher.matches();
    }
}
