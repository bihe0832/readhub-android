package com.bihe0832.readhub.module.about;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.fragment.base.WebViewFragment;

public class AboutAuthorFragment extends WebViewFragment {


    @Override
    public String getLoadUrl() {
        return getString(R.string.link_author);
    }

    @Override
    protected void initView() {
        super.initView();
    }
}
