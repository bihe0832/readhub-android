package com.bihe0832.readhub.module.readhub.topic.api.bean;

import com.bihe0832.readhub.libware.util.TextUtils;

import java.util.ArrayList;

/**
 * @desc Created by erichua on 15/05/2017.
 */

public class Topic {
    private String id;
    private String title;
    private String summary;
    private ArrayList<News> newsArray;
    private ArrayList<News> weiboArray;
    private ArrayList<News> wechatArray;
    private ArrayList<News> relatedTopicArray;
    private String publishUserId;
    private String order;
    private String publishDate;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return TextUtils.getSafeString(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return TextUtils.getSafeString(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return TextUtils.getSafeString(summary);
    }

    public ArrayList<News> getNewsArray() {
        return newsArray;
    }

    public String getOrder() {
        return TextUtils.getSafeString(order);
    }

}
