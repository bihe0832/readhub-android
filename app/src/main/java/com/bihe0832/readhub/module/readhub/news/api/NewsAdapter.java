package com.bihe0832.readhub.module.readhub.news.api;

import android.content.Context;
import android.content.Intent;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.Shakeba;
import com.bihe0832.readhub.framework.activity.MainActivity;
import com.bihe0832.readhub.libware.util.TimeUtils;
import com.bihe0832.readhub.module.readhub.ReadhubOnClickListener;
import com.bihe0832.readhub.module.readhub.adapter.base.SolidRVBaseAdapter;
import com.bihe0832.readhub.module.readhub.news.api.bean.News;

import java.util.List;

public class NewsAdapter extends SolidRVBaseAdapter<News> {

    public NewsAdapter(Context context, List<News> beans) {
        super(context, beans);
    }

    @Override
    public int getItemLayoutID(int vieWType) {
        return R.layout.com_bihe0832_readhub_item_news;
    }

    @Override
    protected void onItemClick(int position) {
        Intent intent = new Intent(mContext, MainActivity.class);
        News info = mBeans.get(position - 1);
        if(null != info){
            intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_URL, info.getUrl());
            intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_TITLE, info.getTitle());
            mContext.startActivity(intent);
        }
    }
    @Override
    protected void onBindDataToView(SolidCommonViewHolder holder, News bean) {
        holder.setText(R.id.title, bean.getTitle());
        holder.setText(R.id.summary, bean.getSummary());
        String subText = String.format(
                Shakeba.getInstance().getStringById(R.string.news_desc),
                bean.getSiteName(), bean.getAuthorName() == null ? "" : bean.getAuthorName(),
                TimeUtils.getDateCompareResultByReadhubDateFormat(bean.getPublishDate()));
        holder.setText(R.id.more,subText);

        ReadhubOnClickListener listener = new ReadhubOnClickListener(bean.getTitle(),bean.getSummary(),bean.getUrl());
        holder.getView(R.id.title).setOnClickListener(listener);
        holder.getView(R.id.summary).setOnClickListener(listener);
        holder.getView(R.id.title).setOnLongClickListener(listener);
        holder.getView(R.id.summary).setOnLongClickListener(listener);
    }
}
