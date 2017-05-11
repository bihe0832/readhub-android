package com.bihe0832.readhub.framework.fragment;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.fragment.base.WebViewFragment;

public class MainFragment extends WebViewFragment {


    @Override
    public String getLoadUrl() {
        return getString(R.string.link_readhub_page);
    }

    @Override
    protected void initView() {
        super.initView();
    }
}