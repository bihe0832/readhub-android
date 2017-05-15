package com.bihe0832.readhub.module.readhub.topic;

import android.content.Context;
import android.widget.LinearLayout;

import com.bihe0832.readhub.R;
import com.bihe0832.readhub.module.readhub.ReadhubOnClickListener;
import com.bihe0832.readhub.module.readhub.adapter.base.SolidRVBaseAdapter;
import com.bihe0832.readhub.module.readhub.topic.request.TopicInfo;
import com.bihe0832.readhub.module.readhub.topic.request.TopicInfoDetail;

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
        LinearLayout moreView = (LinearLayout)holder.getView(R.id.more);
        moreView.removeAllViews();
        if(null != bean.getNewsArrayList() && bean.getNewsArrayList().size() > 0){
            for (TopicInfoDetail info: bean.getNewsArrayList()) {
                TopicDetailItemView view  = new TopicDetailItemView(this.mContext);
                view.initData(info);
                moreView.addView(view);
            }
        }

        ReadhubOnClickListener listener = new ReadhubOnClickListener(moreView,bean.getmTitle(),bean.getmSummary());
        holder.setText(R.id.summary, bean.getmSummary());
        holder.getView(R.id.title).setOnClickListener(listener);
        holder.getView(R.id.title).setOnLongClickListener(listener);
        holder.getView(R.id.summary).setOnClickListener(listener);
        holder.getView(R.id.summary).setOnLongClickListener(listener);

    }
}
