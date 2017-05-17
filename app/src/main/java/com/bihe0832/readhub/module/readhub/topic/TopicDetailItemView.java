package com.bihe0832.readhub.module.readhub.topic;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.activity.MainActivity;
import com.bihe0832.readhub.module.readhub.topic.api.bean.News;

/**
 * Created by hardyshi on 2017/5/15.
 */

public class TopicDetailItemView extends RelativeLayout{
    private Context mContext;

    public TopicDetailItemView(Context context){
        super(context);
        mContext = context;
    }

    public void initData(final News info){

        if(info != null){
            ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.com_bihe0832_readhub_topic_detail_item, this);
            TextView mTitleView = (TextView) findViewById(R.id.common_title);
            TextView mDescView = (TextView) findViewById(R.id.common_desc);
            //增加曝光统计, 首先构建item的统计信息，然后曝光
            mTitleView.setText(Html.fromHtml(info.getTitle()));
            mDescView.setText(Html.fromHtml(info.getSiteName()));
            this.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_URL, info.getUrl());
                    intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_TITLE, info.getTitle());
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
