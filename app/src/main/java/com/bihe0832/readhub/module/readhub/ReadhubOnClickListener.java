package com.bihe0832.readhub.module.readhub;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bihe0832.readhub.framework.activity.MainActivity;
import com.bihe0832.readhub.libware.util.TextUtils;
import com.bihe0832.readhub.module.readhub.news.request.NewsInfo;

/**
 * Created by zixie on 2017/5/13.
 */

public class ReadhubOnClickListener implements View.OnClickListener,View.OnLongClickListener {

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
            Toast.makeText(view.getContext(),"这是一个跳转",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_URL, jumpUrl);
            intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_TITLE, shareTitle);
            view.getContext().startActivity(intent);
        }else{
            mView.setVisibility(View.VISIBLE);
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
