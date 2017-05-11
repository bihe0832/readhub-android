package com.bihe0832.readhub.framework.request;


import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.TextUtils;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author code@bihe0832.com
 */
public abstract class BaseConnection {

    public static final String LOG_TAG = "SHAKEBA_REQUEST";
    protected static final String HTTP_REQ_PROPERTY_CHARSET = "Accept-Charset";
    protected static final String HTTP_REQ_VALUE_CHARSET = "UTF-8";
    protected static final String HTTP_REQ_PROPERTY_CONTENT_TYPE = "Content-Type";
    protected static final String HTTP_REQ_VALUE_CONTENT_TYPE = "application/x-www-form-urlencoded";
    protected static final String HTTP_REQ_PROPERTY_ENCODING = "Accept-Encoding";
    protected static final String HTTP_REQ_PROPERTY_CONTENT_LENGTH = "Content-Length";
    protected static final String HTTP_REQ_METHOD_GET = "GET";
    protected static final String HTTP_REQ_METHOD_POST = "POST";
    protected static final String HTTP_REQ_COOKIE = "Cookie";

    /**
     * 建立连接的超时时间
     */
    protected static final int CONNECT_TIMEOUT = 5 * 1000;
    /**
     * 建立到资源的连接后从 input 流读入时的超时时间
     */
    protected static final int DEFAULT_READ_TIMEOUT = 10 * 1000;

    protected static int sRealReadTimeout = DEFAULT_READ_TIMEOUT;
    public BaseConnection() {

    }

    private void setURLConnectionCommonPara(){
        HttpURLConnection connection = getURLConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(sRealReadTimeout);
        connection.setUseCaches(false);
        connection.setRequestProperty(HTTP_REQ_PROPERTY_CHARSET, HTTP_REQ_VALUE_CHARSET);
        connection.setRequestProperty(HTTP_REQ_PROPERTY_CONTENT_TYPE, HTTP_REQ_VALUE_CONTENT_TYPE);
    }

    private void setURLConnectionCookie(HashMap<String,String> cookieInfo){
        HttpURLConnection connection = getURLConnection();
        String cookieString = connection.getRequestProperty(HTTP_REQ_COOKIE);
        if(!TextUtils.ckIsEmpty(cookieString)){
            cookieString = cookieString + ";";
        }else{
            cookieString = "";
        }
        for (Map.Entry<String, String> entry : cookieInfo.entrySet()) {
            if(TextUtils.ckIsEmpty(entry.getKey()) || TextUtils.ckIsEmpty(entry.getValue())){
                Logger.d("cookie inf is bad");
            }else{
                cookieString = cookieString + entry.getKey() + HttpRequest.HTTP_REQ_ENTITY_MERGE + entry.getValue() + ";";
            }
        }
        connection.setRequestProperty(HTTP_REQ_COOKIE,cookieString);
    }

    public String doRequest(HttpRequest request){
        if(null == getURLConnection()){
            Logger.e(LOG_TAG,"URLConnection is null");
            return "";
        }
        setURLConnectionCommonPara();
        //检查cookie
        if(null != request.cookieInfo && request.cookieInfo.size() > 0){
            setURLConnectionCookie(request.cookieInfo);
        }

        if(null == request.data){
            return doGetRequest();
        }else{
            return doPostRequest(request.data);
        }
    }

    protected abstract HttpURLConnection getURLConnection();
    protected abstract String doGetRequest();
    protected abstract String doPostRequest(byte[] data);
    public abstract String getResponseMessage();
    public abstract int getResponseCode();


}
