package com.ottd.libs.utils;zixie

/**
 * @author zixie
 */
public class MathUtils {

    /**
     * 获取两个数字区间的随机数
     *
     * @param min 下限
     * @param max 上限
     * @return 最终数
     */
    public static int getRandNumByLimit(int min, int max) {
        return (int) java.lang.Math.round(java.lang.Math.random() * (max - min) + min);
    }
}
