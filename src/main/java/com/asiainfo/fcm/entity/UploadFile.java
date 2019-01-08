package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 文件上传信息
 */
public class UploadFile {
    //文件名
    private String fileName;
    //base64编码图片
    private String imgBase64;
    //原始文件名
    private String originalFileName;
    //上传人ID
    private String uploaderId;
    //上传人姓名
    private String uploaderName;
    //上传时间
    private String uploadTime;
    //上传用途
    private int useType;

    public String getFileName() {
        return fileName;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }
}
