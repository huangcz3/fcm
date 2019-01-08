package com.asiainfo.fcm.model;

import java.util.List;

public class UserRecommendations {
    //当前分页索引
    private int pageIndex;
    //分页数据量
    private int pageSize;
    //分页总数
    private int pageCount;
    //总数据量
    private int totalCount;
    //实际数据列表
    private List<UserRecommendationVO> data;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<UserRecommendationVO> getData() {
        return data;
    }

    public void setData(List<UserRecommendationVO> data) {
        this.data = data;
    }
}
