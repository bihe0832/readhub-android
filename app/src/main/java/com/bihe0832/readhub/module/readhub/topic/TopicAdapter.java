package com.bihe0832.readhub.module.readhub.topic;

import android.content.Context;
import android.widget.LinearLayout;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.module.readhub.ReadhubOnClickListener;
import com.bihe0832.readhub.module.readhub.adapter.base.SolidRVBaseAdapter;
import com.bihe0832.readhub.module.readhub.topic.api.bean.News;
import com.bihe0832.readhub.module.readhub.topic.api.bean.Topic;

import java.util.List;

public class TopicAdapter extends SolidRVBaseAdapter<Topic> {


    public TopicAdapter(Context context, List<Topic> beans) {
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
    protected void onBindDataToView(final SolidCommonViewHolder holder, Topic bean) {
        holder.setText(R.id.title, bean.getTitle());
        LinearLayout moreView = holder.getView(R.id.more);
        moreView.removeAllViews();
        if(null != bean.getNewsArray() && bean.getNewsArray().size() > 0){
            for (News info: bean.getNewsArray()) {
                TopicDetailItemView view  = new TopicDetailItemView(this.mContext);
                view.initData(info);
                moreView.addView(view);
            }
        }

        ReadhubOnClickListener listener = new ReadhubOnClickListener(moreView,bean.getTitle(),bean.getSummary());
        holder.setText(R.id.summary, bean.getSummary());
        holder.getView(R.id.title).setOnClickListener(listener);
        holder.getView(R.id.title).setOnLongClickListener(listener);
        holder.getView(R.id.summary).setOnClickListener(listener);
        holder.getView(R.id.summary).setOnLongClickListener(listener);

    }
}
