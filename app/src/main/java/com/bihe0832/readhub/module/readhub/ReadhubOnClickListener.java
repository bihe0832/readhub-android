package com.bihe0832.readhub.module.readhub;

import android.content.Intent;
import android.view.View;

import com.bihe0832.readhub.framework.activity.MainActivity;
import com.bihe0832.readhub.libware.util.TextUtils;

/**
 * Created by zixie on 2017/5/13.
 */

public class ReadhubOnClickListener implements View.OnClickListener,View.OnLongClickListener {

    boolean mViewIsShow = false;
    private View mView;
    private String shareTitle = "";
    private String shareSummary = "";
    private String jumpUrl = "";

    public ReadhubOnClickListener(View view, String title, String summary){
        mView = view;
        shareTitle = title;
        shareSummary = summary;
    }

    public ReadhubOnClickListener(String title, String summary,String url){
        shareTitle = title;
        shareSummary = summary;
        jumpUrl = url;
    }

    @Override
    public void onClick(View view) {
        if(null == mView){
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_URL, jumpUrl);
            intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_TITLE, shareTitle);
            view.getContext().startActivity(intent);
        }else{
            if(mViewIsShow){
                mView.setVisibility(View.GONE);
                mViewIsShow = false;
            }else{
                mView.setVisibility(View.VISIBLE);
                mViewIsShow = true;
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(TextUtils.ckIsEmpty(shareSummary) || TextUtils.ckIsEmpty(shareTitle)){
            return false;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "标题：" + shareTitle + "\n 概述：" +shareSummary);
        sendIntent.setType("text/plain");
        view.getContext().startActivity(Intent.createChooser(sendIntent, shareTitle));
        return true;
    }
}
