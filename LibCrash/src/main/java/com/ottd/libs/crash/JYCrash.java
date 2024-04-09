package com.ottd.libs.crash;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by zixie on 2017/8/21.
 */

public class JYCrash {

    public static void init(Context context, String appid, boolean isDebug) {

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppReportDelay(5000);
        CrashReport.initCrashReport(context, appid, isDebug, strategy);

    }

    public static void setUserID(String openid){
        CrashReport.setUserId(openid);
    }
}
