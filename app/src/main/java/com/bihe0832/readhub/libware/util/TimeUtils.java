package com.bihe0832.readhub.libware.util;

import java.sql.Timestamp;

/**
 * Created by zixie on 2017/5/13.
 */

public class TimeUtils {

    /**
     *
     * @param date YYYY-MM-DD HH:MM:SS
     * @return
     */
    public static Long getTimeStampByDateString(String date){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(date);
            return ts.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     *
     * @param date YYYY-MM-DD HH:MM:SS
     * @return
     */
    public static String getDateCompareResult(String date){
        System.out.println("=====================");
        System.out.println(date);
        Long oldTimestamp =  getTimeStampByDateString(date) / 1000;
        Long currentTimestamp = System.currentTimeMillis() / 1000;
        System.out.println(oldTimestamp);
        System.out.println(currentTimestamp);
        if(oldTimestamp > 0){
            if(currentTimestamp - oldTimestamp > 60){
                int duration = (int)(currentTimestamp - oldTimestamp) / 60;
                System.out.println("duration：" + duration);
                if(duration > 60){
                    duration = duration / 60;
                    System.out.println("duration：" + duration);
                    if(duration > 24){
                        duration = duration / 24;
                        System.out.println("duration：" + duration);
                        if(duration > 30){
                            duration = duration / 30;
                            System.out.println("duration：" + duration);
                            if(duration > 11){
                                return duration / 12 + "年前";
                            }else{
                                return duration + "个月前";
                            }
                        }else{
                            return duration + "天前";
                        }
                    }else{
                        return duration + "小时前";
                    }
                }else{
                    return duration + "分钟前";
                }
            }else{
                return currentTimestamp - oldTimestamp  + "秒前";
            }
        }else{
            return "不久前";
        }
    }
}
