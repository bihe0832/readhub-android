package com.ottd.libs.thread;

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
public class ThreadManager {

    public static final int LOOPER_TYPE_ANDROID_MAIN = 0;
    public static final int LOOPER_TYPE_BG = 1;
    public static final int LOOPER_TYPE_REQUEST = 2;
    public static final int LOOPER_TYPE_TEMP = 3;

    private HandlerThread mBgHandlerThread = null;
    private HandlerThread mRequestHandlerThread = null;
    private HandlerThread mTempHandlerThread = null;

    // 后台线程、用户处理数据存储，业务逻辑等
    private final static String THREAD_NAME_BG = "JYP_BG";
    // 网络请求线程，用于分发网络请求，网络请求的处理利用临时线程池
    private final static String THREAD_NAME_REQUEST = "JYP_REQUEST";
    // 临时线程，用于通过handle处理一些临时逻辑、延迟逻辑
    private final static String THREAD_NAME_TEMP = "JYP_TEMP";
    private Handler mPostDelayTempHandler;

    // 临时线程池，用于通过Runnable处理其余临时逻辑
    private ExecutorService executor;
    private static final int MAX_RUNNING_THREAD = 3;
    private final static String EXECUTOR_NAME_TEMP = "TEMP_THREADS";

    private static volatile ThreadManager instance;
    public static ThreadManager getInstance() {
        if (instance == null) {
            synchronized (ThreadManager.class) {
                if (instance == null) {
                    instance = new ThreadManager();
                }
            }
        }
        return instance;
    }

    private ThreadManager(){
    }

    public Looper getLooper(int type) {
        if (type == LOOPER_TYPE_ANDROID_MAIN) {
            return Looper.getMainLooper();
        } else if (type == LOOPER_TYPE_BG) {
            if(null == mBgHandlerThread){
                mBgHandlerThread = new HandlerThread(THREAD_NAME_BG);
                mBgHandlerThread.start();
            }
            return mBgHandlerThread.getLooper();
        } else if (type == LOOPER_TYPE_REQUEST) {
            if(null == mRequestHandlerThread){
                mRequestHandlerThread = new HandlerThread(THREAD_NAME_REQUEST);
                mRequestHandlerThread.start();
            }
            return mRequestHandlerThread.getLooper();
        }  else {
            if(null == mTempHandlerThread){
                mTempHandlerThread = new HandlerThread(THREAD_NAME_TEMP);
                mTempHandlerThread.start();
            }
            return mTempHandlerThread.getLooper();
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
            mPostDelayTempHandler = new Handler(getLooper(LOOPER_TYPE_TEMP));
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
            namePrefix = EXECUTOR_NAME_TEMP + "-pool-" + poolNumber.getAndIncrement();
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
