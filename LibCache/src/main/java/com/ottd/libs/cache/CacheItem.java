package com.ottd.libs.cache;

/**
 * Created by hardyshi on 2017/9/15.
 */

public class CacheItem {
    private String key;
    private String value;
    private long mCreateTime;
    private long mUpdateTime;
    private long mDeleteTime;

    public CacheItem(String key, String value, long createTime, long updateTime, long deleteTime) {
        this.key = key;
        this.value = value;
        this.mCreateTime = createTime;
        this.mUpdateTime = updateTime;
        this.mDeleteTime = deleteTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        this.mCreateTime = createTime;
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long mUpdateTime) {
        this.mUpdateTime = mUpdateTime;
    }

    public long getDeleteTime() {
        return mDeleteTime;
    }

    public void setDeleteTime(long mDeleteTime) {
        this.mDeleteTime = mDeleteTime;
    }
}