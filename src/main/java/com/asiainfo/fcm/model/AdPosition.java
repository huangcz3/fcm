package com.asiainfo.fcm.model;

import java.util.List;

/**
 * Created by PuMg on 2017/7/17/0017.
 * @Description:广告位下拉
 */
public class AdPosition {

    private String pageName;
    private String pageId;
    private String adsenseName;
    private String adsenseId;
    private String title;
    private String context;
    private String status;
    private List<ImgInfo> imgInfoList;

    public List<ImgInfo> getImgInfoList() {
        return imgInfoList;
    }

    public void setImgInfoList(List<ImgInfo> imgInfoList) {
        this.imgInfoList = imgInfoList;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getAdsenseName() {
        return adsenseName;
    }

    public void setAdsenseName(String adsenseName) {
        this.adsenseName = adsenseName;
    }

    public String getAdsenseId() {
        return adsenseId;
    }

    public void setAdsenseId(String adsenseId) {
        this.adsenseId = adsenseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
