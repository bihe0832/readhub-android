package com.bihe0832.readhub.module.readhub.topic;

import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.module.readhub.ReadhubFragment;
import com.bihe0832.readhub.module.readhub.topic.api.bean.Topic;
import com.bihe0832.readhub.module.readhub.topic.api.bean.TopicRsp;
import com.bihe0832.readhub.module.readhub.topic.api.ITopicService;
import com.bihe0832.readhub.network.ApiClient;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopicFragment extends ReadhubFragment {

    protected TopicAdapter mTopicAdapter;

    @Override
    protected void initView() {
        mTopicAdapter = new TopicAdapter(getMContext(), new ArrayList<Topic>());
        super.initView();
    }
    @Override
    protected void getData() {
        ApiClient.create(ITopicService.class).requestTopicList(mCursor, pageSize).enqueue(new Callback<TopicRsp>() {
            @Override
            public void onResponse(Call<TopicRsp> call, Response<TopicRsp> response) {
                Logger.d(response.body());
                final TopicRsp topicRsp = response.body();
                if (topicRsp == null || topicRsp.getData() == null) {
                    return;
                }
                if(null != topicRsp.getData() && topicRsp.getData().size() > 0 ){
                    ShakebaThreadManager.getInstance().runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mTopicAdapter.addAll(topicRsp.getData());
                        }
                    });
                }
                mCursor = topicRsp.getData().get(topicRsp.getData().size() - 1).getOrder();
                pageSize = topicRsp.getPageSize();
                loadComplete();
            }

            @Override
            public void onFailure(Call<TopicRsp> call, Throwable t) {
                t.printStackTrace();
                Logger.d(t);
            }
        });
    }

//    private class GetTopicResponseHandle implements
//            HttpResponseHandler<GetTopicResponse> {
//
//
//        @Override
//        public void onResponse(final GetTopicResponse response) {
//            Logger.d(response.toString());
//            if(null != response.mInfoList && response.mInfoList.size() > 0 ){
//                ShakebaThreadManager.getInstance().runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mTopicAdapter.addAll(response.mInfoList);
//                    }
//                });
//            }
//            mCursor = response.cursor;
//            pageSize = response.pageSize;
//            loadComplete();
//        }
//    }

    @Override
    protected void clearAdapter() {
        mTopicAdapter.clear();
    }

    @Override
    protected void setAdapter(XRecyclerView recyclerView) {
        recyclerView.setAdapter(mTopicAdapter);
    }
}
