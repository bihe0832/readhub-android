package com.ottd.libs.utils;

/**
 * Created by zixie on 2017/7/24.
 */

public class KVPair {
    public int key;
    public String value;

    @Override
    public String toString(){
        return "key:"+ key + ";value:" + value;
    }
}
