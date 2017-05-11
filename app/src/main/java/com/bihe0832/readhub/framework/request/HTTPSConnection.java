package com.bihe0832.readhub.framework.request;


import com.bihe0832.readhub.libware.file.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * @author code@bihe0832.com
 */
public class HTTPSConnection extends BaseConnection {

    private HttpsURLConnection mConn = null;
    private SSLContext mSSLContext = null;

    public HTTPSConnection(String url) {
        super();
        TrustManager tm = null;
        try {
            tm = MyX509TrustManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null, new TrustManager[] { tm }, null);
            mConn = (HttpsURLConnection)new URL(url).openConnection();
            mConn.setDefaultSSLSocketFactory(mSSLContext.getSocketFactory());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String doGetRequest() {
        String result = "";
        InputStream is = null;
        BufferedReader br = null;
        try {
            mConn.setRequestMethod(HTTP_REQ_METHOD_GET);

            is = mConn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, HTTP_REQ_VALUE_CHARSET));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        }catch (javax.net.ssl.SSLHandshakeException ee){
            Logger.e(LOG_TAG, "javax.net.ssl.SSLPeerUnverifiedException");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            return result;
        }
    }


    @Override
    protected HttpURLConnection getURLConnection() {
        return mConn;
    }

    @Override
    public String doPostRequest(byte[] data) {
        return "";
    }

    @Override
    public String getResponseMessage(){
        try {
            return mConn.getResponseMessage();
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int getResponseCode(){
        try {
            return mConn.getResponseCode();
        }catch (IOException e){
            e.printStackTrace();
            return -1;
        }
    }
}
