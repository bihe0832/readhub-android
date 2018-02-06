package com.ottd.libs.utils;

/**
 * Created by hardyshi on 2017/11/10.
 */

public class TimeUtils {

    public static String getDateCompareResult(long oldTimestamp){
        long minute = 1000 * 60;
        long hour = minute * 60;
        long day = hour * 24;
        long month = day * 30;
        long year = month * 12;
        long currentTimestamp = System.currentTimeMillis();
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
            return diffValue/1000 + "秒前";
        } else {
            return "不久前";
        }
    }
}