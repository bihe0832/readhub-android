package com.bihe0832.readhub.libware.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zixie on 2017/5/13.
 */

public class TimeUtils {

    public static Long getTimeStampByReahubDateString(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String getDateCompareResultByReadhubDateFormat(String date){

        return getDateCompareResult(getTimeStampByReahubDateString(date));
    }

    public static String getDateCompareResult(long oldTimestamp){
        long minute = 1000 * 60;
        long hour = minute * 60;
        long day = hour * 24;
        long month = day * 30;
        long year = month * 12;
        long currentTimestamp = System.currentTimeMillis();
        System.out.println(oldTimestamp);
        System.out.println(currentTimestamp);
        long diffValue = currentTimestamp - oldTimestamp;
        long yearC = diffValue / year;
        long monthC = diffValue / month;
        long weekC = diffValue / (7 * day);
        long dayC = diffValue / day;
        long hourC = diffValue / hour;
        long minC = diffValue / minute;
        if (yearC > 0) {
            return yearC + "年前";
        } else if (monthC > 0) {
            return monthC + "月前";
        } else if (weekC > 0) {
            return weekC + "周前";
        } else if (dayC > 0) {
            return dayC + "天前";
        } else if (hourC > 0) {
            return hourC + "小时前";
        } else if (minC > 0) {
            return minC + "分钟前";
        } else if (diffValue > 0) {
            return diffValue + "秒前";
        } else {
            return "不久前";
        }
    }
}
