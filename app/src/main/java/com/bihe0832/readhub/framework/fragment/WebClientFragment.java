package com.bihe0832.readhub.framework.fragment;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.fragment.base.WebViewFragment;
import com.bihe0832.readhub.libware.util.TextUtils;

public class WebClientFragment extends WebViewFragment {

    private static String sURL = "";

    @Override
    public String getLoadUrl() {
        if(!TextUtils.ckIsEmpty(sURL) && (sURL.startsWith("http") || !sURL.startsWith("https"))){
            return sURL;
        }else{
            return getString(R.string.link_readhub_page);
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    public static void setsURL(String mURL) {
        sURL = mURL;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.loadUrl(getLoadUrl());
    }
}