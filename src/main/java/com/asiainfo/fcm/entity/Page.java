package com.asiainfo.fcm.entity;

import java.util.List;

/**
 * Created by RUOK on 2017/6/30.
 */
public class Page {

    private List<?> dataList;

    private long currentPage;// 当前页

    private long pageSize;// 每页显示记录数

    private long totalRecord;// 总记录数

    private long totalPage;// 总页数

    private long startPosition;// 本页开始记录

    private long endPosition;//本页结束记录

    public Page(long currentPage, long pageSize, long totalRecord) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;

        this.totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.startPosition = (currentPage - 1) * pageSize + 1;
        this.endPosition = currentPage * pageSize;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }
}
