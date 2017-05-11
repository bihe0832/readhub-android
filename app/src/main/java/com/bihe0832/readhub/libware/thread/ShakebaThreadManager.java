package com.bihe0832.readhub.libware.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hardyshi on 16/6/2.
 */
public class ShakebaThreadManager {

    public static final int LOOPER_TYPE_ANDROID_MAIN = 0;
    public static final int LOOPER_TYPE_SHAKEBA_BG = 1;
    public static final int LOOPER_TYPE_SHAKEBA_REQUEST = 2;
    public static final int LOOPER_TYPE_SHAKEBA_TEMP = 3;

    private HandlerThread mSHAKEBABgHandlerThread = null;
    private HandlerThread mSHAKEBARequestHandlerThread = null;
    private HandlerThread mSHAKEBATempHandlerThread = null;

    //SHAKEBA 后台线程、用户处理数据存储，业务逻辑等
    private final static String THREAD_NAME_SHAKEBA_BG = "SHAKEBA_BG";
    //SHAKEBA 网络请求线程，用于分发网络请求，网络请求的处理利用临时线程池
    private final static String THREAD_NAME_SHAKEBA_REQUEST = "SHAKEBA_REQUEST";
    //SHAKEBA 临时线程，用于通过handle处理一些临时逻辑、延迟逻辑
    private final static String THREAD_NAME_SHAKEBA_TEMP = "SHAKEBA_TEMP";
    private Handler mPostDelayTempHandler;

    //SHAKEBA 临时线程池，用于通过Runnable处理其余临时逻辑
    private ExecutorService executor;
    private static final int MAX_RUNNING_THREAD = 3;
    private final static String EXECUTOR_NAME_SHAKEBA_TEMP = "SHAKEBA_TEMP_THREADS";

    private static volatile ShakebaThreadManager instance;
    public static ShakebaThreadManager getInstance() {
        if (instance == null) {
            synchronized (ShakebaThreadManager.class) {
                if (instance == null) {
                    instance = new ShakebaThreadManager();
                }
            }
        }
        return instance;
    }

    private ShakebaThreadManager(){
    }

    public Looper getLooper(int type) {
        if (type == LOOPER_TYPE_ANDROID_MAIN) {
            return Looper.getMainLooper();
        } else if (type == LOOPER_TYPE_SHAKEBA_BG) {
            if(null == mSHAKEBABgHandlerThread){
                mSHAKEBABgHandlerThread = new HandlerThread(THREAD_NAME_SHAKEBA_BG);
                mSHAKEBABgHandlerThread.start();
            }
            return mSHAKEBABgHandlerThread.getLooper();
        } else if (type == LOOPER_TYPE_SHAKEBA_REQUEST) {
            if(null == mSHAKEBARequestHandlerThread){
                mSHAKEBARequestHandlerThread = new HandlerThread(THREAD_NAME_SHAKEBA_REQUEST);
                mSHAKEBARequestHandlerThread.start();
            }
            return mSHAKEBARequestHandlerThread.getLooper();
        }  else {
            if(null == mSHAKEBATempHandlerThread){
                mSHAKEBATempHandlerThread = new HandlerThread(THREAD_NAME_SHAKEBA_TEMP);
                mSHAKEBATempHandlerThread.start();
            }
            return mSHAKEBATempHandlerThread.getLooper();
        }
    }

    public void start(Runnable runnable){
        if(null == executor){
            try {
                executor = Executors.newFixedThreadPool(MAX_RUNNING_THREAD, new CommonThreadFactory());
            } catch (Throwable t) {
                executor = Executors.newCachedThreadPool(new CommonThreadFactory());
            }
        }
        try {
            executor.submit(runnable);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void start(Runnable runnable, int seconds){
        if(null == mPostDelayTempHandler){
            mPostDelayTempHandler = new Handler(getLooper(LOOPER_TYPE_SHAKEBA_TEMP));
        }
        mPostDelayTempHandler.postDelayed(runnable, seconds * 1000);
    }

    private class CommonThreadFactory implements ThreadFactory {
        private final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public CommonThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = EXECUTOR_NAME_SHAKEBA_TEMP + "-pool-" + poolNumber.getAndIncrement();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.MIN_PRIORITY)
                t.setPriority(Thread.MIN_PRIORITY);
            return t;
        }
    }

    public void runOnUIThread(Runnable runnable){
        try{
            new Handler(Looper.getMainLooper()).post(runnable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
