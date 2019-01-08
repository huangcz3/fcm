package com.asiainfo.fcm.controller;


import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.UploadFile;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IFileService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 策略库文件上传和下载
 * Created by ShaoJinyu on 2017/12/13.
 */
@Controller
@RequestMapping("/policyFile")
public class PolicyFileController {
    private static final Logger logger = LoggerFactory.getLogger(PolicyFileController.class);

    @Autowired
    private IFileService fileService;

    @Value("${fcm.policy.file.upload.path}")
    private String uploadFilePath;

    @Value("${fcm.policy.file.download.path}")
    private String downloadFilePath;

    @Value("${fcm.policy.file-type}")
    private String fileType;

    @PostMapping("/upload")
    public void upload(HttpSession session, HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam(value = "useType", defaultValue = "0") int useType,@RequestParam("currentActId") String currentActId) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Result result = this.uploadFile(session, file, useType,currentActId);
        objectMapper.writeValue(printWriter, result);
    }

    private Result uploadFile(HttpSession session, MultipartFile file, int useType, String activityId) throws IOException {
        if (!file.isEmpty()) {
            if( activityId == "" ){
                return ResultUtil.error(ResultEnum.ACTIVITY_ID_DOES_NOT_EXIST);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename.contains("\\")) {
                originalFilename = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);
            }

            logger.info("处理上传文件：{}", originalFilename);

            if (originalFilename.contains(".")) {
                String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
                if (!Arrays.asList(fileType).contains(fileType.toLowerCase())) {
                    return ResultUtil.error(ResultEnum.FILE_TYPE_NOT_MATCH);
                }

                String fileName = IDUtil.generateFileId() + fileType;
                File dest = new File(uploadFilePath+filePath(useType) + File.separator +activityId+ File.separator  + originalFilename);
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

                fileService.saveUploadFile(uploadFile);
                return ResultUtil.success(fileName);
            } else {
                return ResultUtil.error(ResultEnum.UNKNOWN_FILE_TYPE);
            }
        } else {
            return ResultUtil.error(ResultEnum.FILE_IS_BLANK);
        }
    }

    @GetMapping("/downLoad")
    public void downLoad(HttpServletResponse response, @RequestParam("fileName")  String fileName,@RequestParam("fileOriginalName") String fileOriginalName,@RequestParam("useType") int useType,@RequestParam("type") int type,@RequestParam("currentActId") String activityId) {
        try {
            response.setHeader("content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            logger.info("文件名：{}", fileOriginalName);

            Path file = type == 1? Paths.get(uploadFilePath+filePath(useType)+File.separator+activityId, fileOriginalName) :
                                     Paths.get(downloadFilePath+filePath(useType)+File.separator+activityId, fileOriginalName);

            fileName = URLEncoder.encode(fileOriginalName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);

            Files.copy(file, response.getOutputStream());

        } catch (IOException e) {
            logger.error("下载文件异常", e);
        }
    }

    String filePath(int useType){
        String path = "";
        switch (useType){
            case  5 :
                path =  "secene";
                break ;
            case 6 :
                path =   "model";
                break ;
            case 7:
                path =  "scase";
                break ;
        }
        return path;
    }
}
