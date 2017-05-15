package com.bihe0832.readhub.module.readhub.topic.request;


import com.bihe0832.readhub.libware.util.TextUtils;

/**
 * Created by hardyshi on 2017/5/12.
 */

public class TopicInfoDetail {

    protected long mId = 0;
    protected String mAuthorName = "";
    protected String mMobileUrl = "";
    protected String mPublishDate = "";
    protected String mSiteName = "";
    protected String mSummary = "";
    protected String mSummaryAuto = "";
    protected String mTitle = "";
    protected String mUrl = "";

    public String getmAuthorName() {
        return mAuthorName;
    }

    public String getmMobileUrl() {
        return mMobileUrl;
    }

    public String getmSiteName() {
        return mSiteName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmUrl() {
        if(TextUtils.ckIsEmpty(mMobileUrl)){
            return mUrl;
        }else{
            return mMobileUrl;
        }

    }
}