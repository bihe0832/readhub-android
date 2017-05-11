package com.bihe0832.readhub.libware.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * 常用工具类
 */
public class CommonUtils {

    /**
     * 返回bitmap的数组大小
     *
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 解析字符串为整数, 转换出错返回指定默认值
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static int parseInt(String str, int defaultValue) {
        int value = defaultValue;

        if(!TextUtils.ckIsEmpty(str)){
            try {
                 value = Integer.parseInt(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

}
