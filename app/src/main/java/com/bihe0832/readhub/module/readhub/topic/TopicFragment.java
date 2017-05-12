package com.bihe0832.readhub.module.readhub.topic;

import com.bihe0832.readhub.framework.request.HttpResponseHandler;
import com.bihe0832.readhub.framework.request.RequestServer;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.module.readhub.ReadhubFragment;
import com.bihe0832.readhub.module.readhub.topic.request.GetTopicRequest;
import com.bihe0832.readhub.module.readhub.topic.request.GetTopicResponse;
import com.bihe0832.readhub.module.readhub.topic.request.TopicInfo;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;


public class TopicFragment extends ReadhubFragment {

    protected TopicAdapter mTopicAdapter;

    @Override
    protected void initView() {
        mTopicAdapter = new TopicAdapter(getMContext(), new ArrayList<TopicInfo>());
        super.initView();
    }
    @Override
    protected void getData() {
        GetTopicRequest request = new GetTopicRequest(mCursor, pageSize, new GetTopicResponseHandle());
        RequestServer.getInstance().doRequest(request);
    }

    private class GetTopicResponseHandle implements
            HttpResponseHandler<GetTopicResponse> {


        @Override
        public void onResponse(final GetTopicResponse response) {
            Logger.d(response.toString());
            if(null != response.mInfoList && response.mInfoList.size() > 0 ){
                ShakebaThreadManager.getInstance().runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mTopicAdapter.addAll(response.mInfoList);
                    }
                });
            }
            mCursor = response.cursor;
            pageSize = response.pageSize;
            loadComplete();
        }
    }

    @Override
    protected void clearAdapter() {
        mTopicAdapter.clear();
    }

    @Override
    protected void setAdapter(XRecyclerView recyclerView) {
        recyclerView.setAdapter(mTopicAdapter);
    }
}
