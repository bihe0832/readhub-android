package com.ottd.libs.utils.device;

import android.os.Build;
import android.os.SystemProperties;

import com.ottd.libs.utils.TextUtils;


/**
 * Created by hardyshi on 2017/10/31.
 */

public class ManufacturerUtil {

    public static boolean isHuawei() {
        String manufacturer = SystemProperties.get("ro.product.manufacturer", null);
        if (TextUtils.replaceBlank(manufacturer.toLowerCase()).contains("huawei")) {
            return true;
        }

        String fingerprint = SystemProperties.get("ro.build.fingerprint", null);
        if (TextUtils.replaceBlank(fingerprint.toLowerCase()).contains("huawei")) {
            return true;
        }
        return false;
    }

    public static boolean isOppo() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if (!android.text.TextUtils.isEmpty(manufacturer) && manufacturer.toLowerCase().contains("oppo")) {
                return true;
            }

            String fingerprint = Build.FINGERPRINT;
            if (!android.text.TextUtils.isEmpty(fingerprint) && fingerprint.toLowerCase().contains("oppo")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
