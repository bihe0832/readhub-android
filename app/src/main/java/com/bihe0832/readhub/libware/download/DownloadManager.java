package com.bihe0832.readhub.libware.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;


import com.bihe0832.readhub.libware.encrypt.HexUtils;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.libware.util.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by hardyshi on 16/12/1.
 */
public class DownloadManager {

    private static volatile DownloadManager instance;
    public static final String LOG_TAG = "YSDK DOWNLOAD";

    private Handler mDownLoadHandler = null;

    private static final int MSG_ADD_NEW_ITEM = 1;

    private DownloadManager(){
        Looper tempLooper = ShakebaThreadManager.getInstance().getLooper(
                ShakebaThreadManager.LOOPER_TYPE_SHAKEBA_TEMP);
        mDownLoadHandler = new HandlerInTemp(tempLooper);
    };
    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    private class HandlerInTemp extends Handler {
        public HandlerInTemp(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_NEW_ITEM: {
                    if(msg.obj instanceof DownloadItem){
                        DownloadItem item = (DownloadItem)msg.obj;
                        addToDownloadQueueAsync(item);
                    }else{
                        Logger.w(LOG_TAG,"addToDownloadQueueAsync para is bad");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
    public void addToDownloadQueue(URL url, String filePath, String hashValue) {
        Logger.d(LOG_TAG,url);
        Logger.d(LOG_TAG,filePath);
        Logger.d(LOG_TAG,hashValue);
        if(null == url || TextUtils.ckIsEmpty(filePath) || TextUtils.ckIsEmpty(hashValue)){
            Logger.w(LOG_TAG,"url or filePath or hashValue is null");
            return ;
        }

        DownloadItem tempItem = new DownloadItem(url,filePath,hashValue);
        Message message = mDownLoadHandler.obtainMessage(MSG_ADD_NEW_ITEM,tempItem);
        mDownLoadHandler.sendMessage(message);
    }


    public void addToDownloadQueueAsync(final DownloadItem item){
        ShakebaThreadManager.getInstance().start(new Runnable() {
            @Override
            public void run() {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    HttpURLConnection conn = (HttpURLConnection) item.mFileUrl.openConnection(); // 建立一个远程连接句柄，此时尚未真正连接
                    conn.setConnectTimeout(5 * 1000); // 设置连接超时时间为5秒
                    conn.setRequestMethod("GET"); // 设置请求方式为GET
                    conn.setRequestProperty(
                            "Accept",
                            "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                    conn.setRequestProperty("Charset", "UTF-8"); // 设置客户端编码
                    conn.setRequestProperty(
                            "User-Agent",
                            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)"); // 设置用户代理
                    conn.setRequestProperty("Connection", "Keep-Alive"); // 设置Connection的方式
                    conn.connect(); // 和远程资源建立真正的连接，但尚无返回的数据流

                    item.mFileLength = conn.getContentLength();
                    byte[] buffer = new byte[4096]; // 下载的缓冲池为4KB
                    bis = new BufferedInputStream(conn.getInputStream());
                    File tempPic = new File(item.mLocalFilePath+"_temp");
                    bos = new BufferedOutputStream(new FileOutputStream(tempPic));

                    long downloadLength = 0;// 当前已下载的文件大小
                    int bufferLength = 0;
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    String fileMd5 = "";
                    while ((bufferLength = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bufferLength);
                        md5.update(buffer, 0, bufferLength);
                        bos.flush();
                        // 计算当前下载进度
                        downloadLength += bufferLength;
                        item.mPercent = downloadLength / item.mFileLength;
                    }
                    byte[] bs = md5.digest();
                    fileMd5 = HexUtils.bytes2HexStr(bs).toLowerCase(Locale.CHINA);
                    //下载完成，对比md5
                    if(fileMd5.equalsIgnoreCase(item.mHashValue)){
                        File localFile = new File(item.mLocalFilePath);
                        boolean flag = tempPic.renameTo(localFile);
                        if(flag){
                            Logger.d(LOG_TAG,"rename succ：" + item.mLocalFilePath);
                        }else{
                            Logger.d(LOG_TAG,"rename failed：" + item.mLocalFilePath);
                        }
                    }else{
                        Logger.w(LOG_TAG,"fileMd5:"+fileMd5+";hashValue:"+ item.mHashValue);
                        delFileByPath(item.mLocalFilePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 这里处理下载失败（建议发送下载失败的消息）
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void delFileByPath(String filePath){
        if(TextUtils.ckIsEmpty(filePath)){
            return ;
        }
        File file = new File(filePath);
        if(null != file){
            file.delete();
        }
    }

    private class DownloadItem{
        // 下载进度
        public float mPercent = 0;
        // 下载路径
        public URL mFileUrl;
        // 要下载文件的hash值
        public String mHashValue;
        // 下载的文件大小
        public long mFileLength;
        // 文件的保存路径
        public String mLocalFilePath;
        public DownloadItem(URL url, String filePath, String hashValue) {
            this.mFileUrl = url;
            this.mLocalFilePath = filePath;
            this.mHashValue = hashValue;
        }
    }

    public static void setBitmapToImgView(final ImageView img,final String urlStr){
        if(!TextUtils.ckIsEmpty(urlStr)){
            ShakebaThreadManager.getInstance().start(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlStr);
                        final Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                        if( bitmap != null){
                            img.post(new Runnable() {
                                @Override
                                public void run() {
                                    img.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}


