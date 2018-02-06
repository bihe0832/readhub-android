package com.ottd.libs.cache;

import android.content.Context;
import android.support.annotation.NonNull;


import com.ottd.libs.utils.APKUtils;
import com.ottd.libs.utils.MD5;
import com.ottd.libs.utils.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created on 2016/11/29.
 *
 * @author WangYi
 */

public final class CacheManager {

    //一个key对应的缓存文件的数量
    private static final int CACHE_FILES_NUM = 1;
    private static final int DISK_CACHE_INDEX = 0;
    private DiskLruCache mDiskLruCache;
    private MemLruCache mMemLruCache;
    private boolean isInit = false;
    private volatile static CacheManager mCacheManager;
    public static CacheManager getInstance() {
        if (mCacheManager == null) {
            synchronized (CacheManager.class) {
                if (mCacheManager == null) {
                    mCacheManager = new CacheManager();
                }
            }
        }
        return mCacheManager;
    }

    private CacheManager() {}

    public void init(Context ctx, long cacheSizeInDisk, int cacheSizeInMem, String cacheFilePath) {
        if(isInit){
            return;
        }else{
            isInit = true;
        }
        File diskCacheDir = new File(cacheFilePath);
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (diskCacheDir.getUsableSpace() > cacheSizeInDisk) {
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, APKUtils.getAppVersionCode(ctx), CACHE_FILES_NUM, cacheSizeInDisk);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMemLruCache = new MemLruCache(cacheSizeInMem);
    }

    public void test(){
        CacheItem a = CacheManager.getInstance().getCacheItemInMem("12");
        String b = CacheManager.getInstance().getValueInDisk("12");
        String c = CacheManager.getInstance().getValueInMem("12");
        CacheManager.getInstance().put("12","23",120,true);
        CacheItem d = CacheManager.getInstance().getCacheItemInMem("12");
        String e = CacheManager.getInstance().getValueInDisk("12");
        String f = CacheManager.getInstance().getValueInMem("12");
    }

    public void put(@NonNull String key,@NonNull  String value,int duration, boolean synchronizeWriteToDisk){
        key = MD5.getMd5(key);
        mMemLruCache.put(key,value,duration);
        if(synchronizeWriteToDisk){
            putDiskCache(key,value);
        }else{
            putDiskCacheAsync(key, value);
        }
    }

    /**
     * 仅从内存缓存读取内容
     */
    public String getValueInMem(@NonNull String key){
        return mMemLruCache.get(MD5.getMd5(key));
    }

    /**
     * 从内存缓存读取所有数据
     */
    public CacheItem getCacheItemInMem(@NonNull String key){
        return mMemLruCache.getItem(MD5.getMd5(key));
    }

    /**
     * 仅从本地缓同步存读取内容
     */
    public String getValueInDisk(@NonNull String key){
        return getCache(MD5.getMd5(key),null);
    }

    /**
     * 仅从本地缓存异步读取内容
     */
    public void getValueInDiskAsync(@NonNull final String key,final GetCacheCallback callback){
        new Thread() {
            @Override
            public void run() {
                getCache(MD5.getMd5(key), callback);
            }
        }.start();
    }

    /**
     * 优先从内存缓存读取内容，如果内存没有再去尝试本地缓存并写入内存
     *
     * @param duration 写入缓存时对应的有效期
     */
    public String getValueMemFirst(@NonNull String key, int duration){
        key = MD5.getMd5(key);
        String result = mMemLruCache.get(key);
        if(TextUtils.ckIsEmpty(result)){
            result = getValueInDisk(key);
            if(!TextUtils.ckIsEmpty(result)){
                mMemLruCache.put(key,result,duration);
            }
        }
        return result;
    }

    /**
     * 优先从内存缓存读取内容，如果内存没有再去尝试本地缓存
     */
    public String getValueMemFirst(@NonNull String key){
        key = MD5.getMd5(key);
        String result = mMemLruCache.get(key);
        if(TextUtils.ckIsEmpty(result)){
            result = getValueInDisk(key);
        }
        return result;
    }

    /**
     * 同步写入本地缓存
     */
    private void putDiskCache(String key, String value) {
        if (mDiskLruCache == null) return;
        OutputStream os = null;
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if(null == editor){
                return;
            }
            os = editor.newOutputStream(DISK_CACHE_INDEX);
            os.write(value.getBytes());
            os.flush();
            editor.commit();
            mDiskLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 异步写入本地缓存
     */
    private void putDiskCacheAsync(final String key, final String value) {
        new Thread() {
            @Override
            public void run() {
                putDiskCache(key, value);
            }
        }.start();
    }

    /**
     * 从本地缓存读取数据
     * @param key   对应key
     * @param callback 异步读取时对应的回调
     * @return
     */
    private String getCache(String key , GetCacheCallback callback) {
        if (mDiskLruCache == null) {
            if(null != callback) callback.onFail(GetCacheCallback.NoData);
            return null;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                fis = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = fis.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                byte[] data = bos.toByteArray();
                if(null != callback) callback.onSuccess(new String(data));
                return new String(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(null != callback) callback.onFail(GetCacheCallback.GetDataError);
        return null;
    }
}
