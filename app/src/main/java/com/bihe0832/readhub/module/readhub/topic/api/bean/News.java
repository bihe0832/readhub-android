package com.bihe0832.readhub.module.readhub.topic.api.bean;

import com.bihe0832.readhub.libware.util.TextUtils;

/**
 * @desc Created by erichua on 15/05/2017.
 */

public class News {
    private String id;
    private String url;
    private String title;
    private String groupId;
    private String siteName;
    private String mobileUrl;
    private String authorName;
    private String duplicateId;
    private String publishDate;

    public String getId() {
        return TextUtils.getSafeString(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return TextUtils.getSafeString(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return TextUtils.getSafeString(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSiteName() {
        return TextUtils.getSafeString(siteName);
    }
}
