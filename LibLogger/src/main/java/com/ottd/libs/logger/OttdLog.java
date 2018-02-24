package com.ottd.libs.logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by hardyshi on 2017/8/31.
 */

public class OttdLog {
    public static boolean sShowDebugLogSwitch = true;

    public static void init(Context ctx, boolean showDebugLog){
        Logger.init(ctx,Logger.LOG_BOTH);
        sShowDebugLogSwitch = showDebugLog;
    }

    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void e(String tag, Throwable throwable) {
        Logger.e(tag,throwable);
    }

    public static void e(String tag, String msg) {
        Logger.e(tag,msg);
    }

    public static void w(String msg) {
        Logger.w(msg);
    }

    public static void w(String tag, String msg) {
        Logger.w(tag,msg);
    }

    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void d(Object msg) {
        Logger.d(msg);
    }

    public static void d(String tag, Object msg) {
        Logger.d(tag,msg);
    }

    public static void d(Bundle b) {
        Logger.d(b);
    }

    public static void d(Intent i) {
        Logger.d(i);
    }

    public static void d(String tag, Intent i) {
        Logger.d(tag,i);
    }

    public static void d(String tag, String msg) {
        Logger.d(tag,msg);
    }
}
