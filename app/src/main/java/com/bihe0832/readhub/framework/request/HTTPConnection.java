package com.bihe0832.readhub.framework.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author code@bihe0832.com
 */
public class HTTPConnection extends BaseConnection {

    private HttpURLConnection mConn = null;
    public HTTPConnection(String url) {
        super();
        try {
            mConn = (HttpURLConnection)new URL(url).openConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String doPostRequest(byte[] data) {
        BufferedReader br = null;
        InputStream inptStream = null;
        OutputStream outputStream = null;
        try {
            mConn.setRequestMethod(HTTP_REQ_METHOD_POST);
            mConn.setRequestProperty(HTTP_REQ_PROPERTY_CONTENT_LENGTH, String.valueOf(data.length));

            //获得输出流，向服务器写入数据
            outputStream = mConn.getOutputStream();
            outputStream.write(data);

            int response = mConn.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                inptStream = mConn.getInputStream();
                br = new BufferedReader(new InputStreamReader(inptStream, HTTP_REQ_VALUE_CHARSET));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inptStream != null) {
                    inptStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    protected HttpURLConnection getURLConnection() {
        return mConn;
    }

    @Override
    public String doGetRequest() {
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
