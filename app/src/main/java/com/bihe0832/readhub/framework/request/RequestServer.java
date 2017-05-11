package com.bihe0832.readhub.framework.request;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.libware.util.TextUtils;

import java.net.HttpURLConnection;

public class RequestServer {
    private static volatile RequestServer instance;
    private static final String LOG_TAG = "SERVER";
    private Handler mCallHandler;
    private static final int MSG_REQUEST = 0;

    public static RequestServer getInstance () {
        if (instance == null) {
            synchronized (RequestServer.class) {
                if (instance == null) {
                    instance = new RequestServer();
                }
            }
        }
        return instance;
    }

    private RequestServer() {}

    public void init () {

        Looper callLooper = Shakeba.getInstance().getLooper(ShakebaThreadManager.LOOPER_TYPE_SHAKEBA_REQUEST);
        mCallHandler = new Handler(callLooper) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_REQUEST:
                        if(msg.obj != null && msg.obj instanceof HttpRequest){
                            executeRequest((HttpRequest)msg.obj);
                        }else{
                            Logger.d(LOG_TAG,msg.toString());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    
    public void doRequest(HttpRequest request) {
        Message msg = mCallHandler.obtainMessage();
        msg.what = MSG_REQUEST;
        msg.obj = request;
        mCallHandler.sendMessage(msg);
    }

    private void executeRequest(final HttpRequest request){
        ShakebaThreadManager.getInstance().start(new Runnable() {
            @Override
            public void run() {
                executeRequestInExecutor(request);
            }
        });
    }
    private void executeRequestInExecutor(HttpRequest request){
        request.requestTime = System.currentTimeMillis() / 1000;

        String url = request.getUrl();
        if(Shakeba.getInstance().isDebug()){
            Logger.w(LOG_TAG,"=======================================");
            Logger.w(LOG_TAG,request.getClass().toString());
            Logger.w(LOG_TAG,url);
            Logger.w(LOG_TAG,"=======================================");
        }
        BaseConnection connection = null;
        if(url.startsWith("https:")){
            connection = new HTTPSConnection(url);
        }else{
            connection = new HTTPConnection(url);
        }

        String result = connection.doRequest(request);
        if(Shakeba.getInstance().isDebug()){
            Logger.w(LOG_TAG,"=======================================");
            Logger.w(LOG_TAG,request.getClass().toString());
            Logger.w(LOG_TAG,result);
            Logger.w(LOG_TAG, String.valueOf(connection.getResponseCode()));
            Logger.w(LOG_TAG,connection.getResponseMessage());
            Logger.w(LOG_TAG,"=======================================");
        }

        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            request.mHttpResponseHandler.onSuccess(connection.getResponseCode(), result);
        }else{
            if (TextUtils.ckIsEmpty(result)) {
                if(Shakeba.getInstance().isDebug()) {
                    Logger.e(LOG_TAG, request.getClass().getName());
                }
                Logger.e(LOG_TAG,"responseBody is null");
                if(TextUtils.ckIsEmpty(connection.getResponseMessage())){
                    request.mHttpResponseHandler.onFailure(connection.getResponseCode(), "");
                }else{
                    request.mHttpResponseHandler.onFailure(connection.getResponseCode(),connection.getResponseMessage());
                }
            } else {
                request.mHttpResponseHandler.onSuccess(connection.getResponseCode(), result);
            }
        }
    }
}
