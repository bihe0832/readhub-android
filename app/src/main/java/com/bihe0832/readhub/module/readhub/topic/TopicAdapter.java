package com.bihe0832.readhub.module.readhub.topic;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.activity.MainActivity;
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
//        Intent intent = new Intent(mContext, MainActivity.class);
//        TopicInfo info = mBeans.get(position - 1);
        //TODO 如果没有summay 直接出列表
    }

    @Override
    protected void onBindDataToView(final SolidCommonViewHolder holder, TopicInfo bean) {
        holder.setText(R.id.title, bean.getmTitle());
        holder.setText(R.id.summary, bean.getmSummary());
        holder.setText(R.id.test, "test");
        holder.getView(R.id.summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.getView(R.id.test).setVisibility(View.VISIBLE);
            }
        });
    }
}
