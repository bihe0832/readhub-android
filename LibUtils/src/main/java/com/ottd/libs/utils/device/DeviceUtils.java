package com.ottd.libs.utils.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.ottd.libs.utils.TextUtils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static com.ottd.libs.utils.device.ExternalStorage.read;

/**
 * Created by hardyshi on 2017/10/12.
 */

public class DeviceUtils {

    private static String sImei = "";
    private static final String DEFAULT_IMEI  = "000000";

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    public static String getImei(Context context) {
        if(!TextUtils.ckIsEmpty(sImei)){
            return sImei;
        }
        try{
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (tm == null) {
                return DEFAULT_IMEI;
            }
            String imei = tm.getDeviceId();

            if (TextUtils.ckIsEmpty(imei)) {
                return DEFAULT_IMEI;
            }
            sImei = imei;

        }catch (Exception e){
            e.printStackTrace();
            return DEFAULT_IMEI;
        }

        return sImei;
    }

    public static String getResolution(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics == null) {
            return "";
        } else {
            return metrics.widthPixels + "*" + metrics.heightPixels;
        }
    }

    public static String getAndroidIdInPhone(Context mContext) {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getAndroidIdInSdCard(Context mContext) {
        if (ExternalStorage.isSDCardExistAndCanRead()) {
            String path = ExternalStorage.getCommonRootDir(mContext) + "/" + ".aid";
            return read(path);
        }
        return "";
    }

    public static String getImsi(Context mContext) {
        int result = mContext.checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        try {
            final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String rs = tm.getSubscriberId();
            if (TextUtils.ckIsEmpty(rs)) {
                return "";
            }
            return rs;
        } catch (Exception e) {
            return "";
        }
    }


    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    // 获取手机型号
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取本机Mac地址
     *
     * @param context
     * @return mac地址
     */
    public static String getLocalMacAddress(Context context) {
        String macAddress = getMacAddressByWifiInfo(context);
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return "please open wifi";
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
     *
     * @param context 上下文
     * @return MAC地址
     */
    @SuppressLint("HardwareIds")
    private static String getMacAddressByWifiInfo(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @return MAC地址
     */
    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> temp = NetworkInterface.getNetworkInterfaces();
            if(null != temp){
                List<NetworkInterface> nis = Collections.list(temp);
                if (null!= nis) {
                    for (NetworkInterface ni : nis) {
                        if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                        byte[] macBytes = ni.getHardwareAddress();
                        if (macBytes != null && macBytes.length > 0) {
                            StringBuilder res1 = new StringBuilder();
                            for (byte b : macBytes) {
                                res1.append(String.format("%02x:", b));
                            }
                            return res1.deleteCharAt(res1.length() - 1).toString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }
}
