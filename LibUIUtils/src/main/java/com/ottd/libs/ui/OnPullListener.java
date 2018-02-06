package com.ottd.libs.ui;

import android.view.View;

/**
 * Created by Cmad on 2015/5/12.
 */
public interface OnPullListener {
    public void onPulling(View headview);
    public void onCanRefreshing(View headview);
    public void onRefreshing(View headview);
}
