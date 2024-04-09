package com.ottd.libs.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zixie on 16/11/21.
 */
public class TextUtils {

    public static final String STRING_HTML_SPACE = "&nbsp;";
    /**
     * 判断字符串是否为空
     *
     * @param s
     *            需要判断的字符串
     * @return boolean 为空返回true
     */
    public static boolean ckIsEmpty(String s) {
        if (s == null || s.trim().equals("") || s.trim().equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean ckNonEmpty(String... argvs) {
        for (String arg : argvs) {
            if (ckIsEmpty(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤字符串的空格
     * */
    public static String replaceBlank(String str) {
        if(null == str) {
            return null;
        }
        String dest = "";
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
        dest = m.replaceAll("");
        return dest;
    }

    /**
     * 分割字符串
     * @param line			原始字符串
     * @param seperator		分隔符
     * @return				分割结果
     */
    public static String[] split(String line, String seperator) {
        if (line == null || seperator == null || seperator.length() == 0)
            return null;
        ArrayList<String> list = new ArrayList<String>();
        int pos1 = 0;
        int pos2;
        for (; ; ) {
            pos2 = line.indexOf(seperator, pos1);
            if (pos2 < 0) {
                list.add(line.substring(pos1));
                break;
            }
            list.add(line.substring(pos1, pos2));
            pos1 = pos2 + seperator.length();
        }
        // 去掉末尾的空串，和String.split行为保持一致
        for (int i = list.size() - 1; i >= 0 && list.get(i).length() == 0; --i) {
            list.remove(i);
        }
        return list.toArray(new String[0]);
    }

    /**
     * 指定长度的随机字符串
     *
     * @param len
     *            随机字符串长度
     * @return 获取到的随机字符串
     */
    public static String getRandomString(int len) {
        String returnStr = "";
        char[] ch = new char[len];
        Random rd = new Random();
        for (int i = 0; i < len; i++) {
            ch[i] = (char) (rd.nextInt(9) + 97);
        }
        returnStr = new String(ch);
        return returnStr;
    }

    public static byte[] getBytesUTF8(String str) {
       	try {
       		return str.getBytes("UTF-8");
       	} catch (UnsupportedEncodingException e) {
       		return null;
       	}
    }

    public static String getSafeString(String str) {
        if(ckIsEmpty(str)){
            return "";
        }else {
            return str;
        }
    }

    public static String getSafeStringWithMaxlength(String str, int length) {
        String tempStr = TextUtils.getSafeString(str);
        if(length < 0 ){
            return tempStr;
        }else{
            if(tempStr.length() > length){
                return tempStr.substring(0, length - 1) + "...";
            }else{
                return tempStr;
            }
        }
    }

    public static String getSpecialText(String text, int color){
        return "<font color='"+color + "'>" + text + "</font>";
    }

    public static Spannable getLinkText(String text){
        Spannable s = (Spannable) Html.fromHtml(text);
        for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }
        return s;
    }


}
