package com.ottd.libs.web;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;

/**
 * Created by hardyshi on 2017/7/21.
 */

public class X5ObserWebView extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public X5ObserWebView(final Context context) {
        super(context);
    }

    public X5ObserWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t);
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }


    public interface OnScrollChangedCallback {
        void onScroll(int l, int t);
    }
}