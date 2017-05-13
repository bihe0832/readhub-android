package com.bihe0832.readhub.framework.fragment;

import android.view.View;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.fragment.base.WebViewFragment;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.util.TextUtils;

public class WebClientFragment extends WebViewFragment {

    private static String sURL = "";

    @Override
    public String getLoadUrl() {
        String url = getsURL();
        if(TextUtils.ckIsEmpty(url)){
            url = getString(R.string.link_readhub_page);
            Logger.d("bad murl");
        }
        return url;
    }


    @Override
    protected void initView() {
        super.initView();
    }

    private static String getsURL() {
        if(!TextUtils.ckIsEmpty(sURL) && (sURL.startsWith("http") || !sURL.startsWith("https"))){
            return sURL;
        }else{
            return "";
        }

    }

    public static void setmURL(String mURL) {
        sURL = mURL;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.loadUrl(getLoadUrl());
    }
}