package com.bihe0832.readhub.module.readhub.topic.api.bean;

import java.util.ArrayList;

/**
 * @desc Created by erichua on 15/05/2017.
 */

public class Topic {
    private String id;
    private String title;
    private String summary;
    private ArrayList<News> newsArray;
    private String weiboArray;
    private String wechatArray;
    private String relatedTopicArray;
    private String publishUserId;
    private String order;
    private String publishDate;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<News> getNewsArray() {
        return newsArray;
    }

    public void setNewsArray(ArrayList<News> newsArray) {
        this.newsArray = newsArray;
    }

    public String getWeiboArray() {
        return weiboArray;
    }

    public void setWeiboArray(String weiboArray) {
        this.weiboArray = weiboArray;
    }

    public String getWechatArray() {
        return wechatArray;
    }

    public void setWechatArray(String wechatArray) {
        this.wechatArray = wechatArray;
    }

    public String getRelatedTopicArray() {
        return relatedTopicArray;
    }

    public void setRelatedTopicArray(String relatedTopicArray) {
        this.relatedTopicArray = relatedTopicArray;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
