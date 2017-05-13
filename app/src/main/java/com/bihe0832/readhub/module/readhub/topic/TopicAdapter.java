package com.bihe0832.readhub.module.readhub.topic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.libware.ui.ToastUtil;
import com.bihe0832.readhub.libware.util.TextUtils;
import com.bihe0832.readhub.module.readhub.ReadhubOnClickListener;
import com.bihe0832.readhub.module.readhub.adapter.base.SolidRVBaseAdapter;
import com.bihe0832.readhub.module.readhub.topic.request.TopicInfo;

import java.util.List;

public class TopicAdapter extends SolidRVBaseAdapter<TopicInfo> {

    public TopicAdapter(Context context, List<TopicInfo> beans) {
        super(context, beans);
    }

    @Override
    public int getItemLayoutID(int vieWType) {
        return R.layout.com_bihe0832_readhub_item_topic;
    }

    @Override
    protected void onItemClick(int position) {

    }

    @Override
    protected void onBindDataToView(final SolidCommonViewHolder holder, TopicInfo bean) {
        holder.setText(R.id.title, bean.getmTitle());
        TextView moreView = holder.getView(R.id.more);
        ReadhubOnClickListener listener = new ReadhubOnClickListener(moreView,bean.getmTitle(),bean.getmSummary());
        if (TextUtils.ckIsEmpty(bean.getmSummary())) {
            moreView.setVisibility(View.VISIBLE);
            holder.getView(R.id.title).setOnLongClickListener(listener);
            holder.getView(R.id.summary).setOnLongClickListener(listener);
        } else {
            holder.setText(R.id.summary, bean.getmSummary());
            holder.getView(R.id.title).setOnClickListener(listener);
            holder.getView(R.id.summary).setOnClickListener(listener);
            holder.getView(R.id.title).setOnLongClickListener(listener);
            holder.getView(R.id.summary).setOnLongClickListener(listener);
        }
    }
}
