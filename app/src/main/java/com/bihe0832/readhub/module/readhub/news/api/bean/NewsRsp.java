package com.bihe0832.readhub.module.readhub.news.api.bean;

import java.util.List;

/**
 * @desc Created by erichua on 16/05/2017.
 */

public class NewsRsp {
    private List<News> data;
    private int pageSize;
    private int totalItems;
    private int totalPages;

    public List<News> getData() {
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
