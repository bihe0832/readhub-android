package com.ottd.libs.cache;

import android.support.annotation.NonNull;
import android.util.LruCache;

import com.ottd.libs.cache.CacheItem;

/**
 * Created by hardyshi on 2017/9/15.
 */

public class MemLruCache {

    //默认内存超时时间1分钟
    private static final long DEFAULT_DURING = 60 * 10;
    private final LruCache<String, CacheItem> mLruCache;

    public MemLruCache(int maxSize) {
        mLruCache = new LruCache<>(maxSize);
    }

    public String get(@NonNull String key) {
        CacheItem cacheItem = getItem(key);
        if(null != cacheItem){
            return cacheItem.getValue();
        }else{
            return "";
        }
    }

    public CacheItem getItem(@NonNull String key) {
        CacheItem cacheItem = mLruCache.get(key);
        if (cacheItem == null) {
            return null;
        }

        if (System.currentTimeMillis() / 1000 < cacheItem.getDeleteTime()) {
            return cacheItem;
        } else {
            mLruCache.remove(key);
            return null;
        }
    }

    public String put(@NonNull String key, @NonNull String value, long during) {
        if (during < 1) {
            mLruCache.remove(key);
            return null;
        }

        long time = System.currentTimeMillis() / 1000;
        CacheItem cacheItem = new CacheItem(key, value, time, time, time + during);
        CacheItem previous = mLruCache.put(key, cacheItem);

        if (previous != null) {
            return previous.getValue();
        } else {
            return null;
        }
    }

    public String put(@NonNull String key, @NonNull String value) {
        return put(key, value,DEFAULT_DURING);
    }
}