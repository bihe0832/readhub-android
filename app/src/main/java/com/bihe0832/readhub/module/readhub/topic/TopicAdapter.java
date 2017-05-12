package com.bihe0832.readhub.module.readhub.topic;

import android.content.Context;
import android.content.Intent;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.framework.activity.MainActivity;
import com.bihe0832.readhub.framework.adapter.base.SolidRVBaseAdapter;

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
        Intent intent = new Intent(mContext, MainActivity.class);
        TopicInfo info = mBeans.get(position - 1);
        //TODO 逻辑处理
        if(null != info){
            intent.putExtra(MainActivity.INTENT_EXTRA_KEY_ITEM_URL, info.mId);
            mContext.startActivity(intent);
        }
    }
    @Override
    protected void onBindDataToView(SolidCommonViewHolder holder, TopicInfo bean) {
        holder.setText(R.id.title, bean.getmTitle());
        holder.setText(R.id.summary, bean.getmSummary());
    }
}
