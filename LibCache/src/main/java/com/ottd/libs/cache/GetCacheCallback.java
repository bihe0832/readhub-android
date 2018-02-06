package com.ottd.libs.cache;


public interface GetCacheCallback {

    int NoData = 0;
    int GetDataError = 1;
    void onSuccess(String cache);

    void onFail(int code);
}
