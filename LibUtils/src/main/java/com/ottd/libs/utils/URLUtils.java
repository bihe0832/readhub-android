package com.ottd.libs.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hardyshi on 16/11/21.
 */
public class URLUtils {

    public static final String HTTP_REQ_ENTITY_START = "?";
    public static final String HTTP_REQ_ENTITY_MERGE = "=";
    public static final String HTTP_REQ_ENTITY_JOIN = "&";

    /**
     * @param source
     *            传入带参数的url,如:http://www.qq.com/ui/oa/test.html?name=hao&id=123
     * @return 去掉参数的url,如:http://www.qq.com/ui/oa/test.html
     */
    public static String getNoQueryUrl(String source) {
        String dest = null;
        try {
            URL sUrl = new URL(source);
            URL dUrl = new URL(sUrl.getProtocol(), sUrl.getHost(),
                    sUrl.getPort(), sUrl.getPath());
            dest = dUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return dest;
    }

    public static String getUrlEncodeValue(String origValue) {
        if (origValue == null) {
            origValue = "";
        }
        return encode(origValue);

    }

    public static String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuffer buf = new StringBuffer(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7'
                    && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }


    public static String marge(String url, String para) {
        if(TextUtils.ckIsEmpty(url)){
            return "";
        }
        if(url.contains(HTTP_REQ_ENTITY_START)){
            url += HTTP_REQ_ENTITY_JOIN;
        }else {
            url += HTTP_REQ_ENTITY_START;
        }
        if(!TextUtils.ckIsEmpty(para)){
            url += para;
        }
        return url;
    }
}
