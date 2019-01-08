package com.asiainfo.fcm.model;

public class PageRequest {
    private int pageIndex;
    private int pageSize;

    public PageRequest() {
    }

    public PageRequest(int currentPage, int pageSize) {
        this.pageIndex = currentPage;
        this.pageSize = pageSize;
    }

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
}
