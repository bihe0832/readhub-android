package com.ottd.libs.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by hardyshi on 2017/9/15.
 */

public class APKUtils {

    /**
     * 获取APP版本号
     */
    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? 0 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? "" : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi == null ? "" : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static PackageInfo getApkInfoByPackageName(Context ctx, String packageName) {
        String pkgName = packageName.trim();
        PackageManager pm = ctx.getPackageManager();
        List<PackageInfo> packageList = pm.getInstalledPackages(0);

        if (null == packageList) {
            return null;
        }

        for (PackageInfo item : packageList) {
            if (item.applicationInfo.packageName.equalsIgnoreCase(pkgName)) {
                return item;
            }
        }
        return null;
    }

}
