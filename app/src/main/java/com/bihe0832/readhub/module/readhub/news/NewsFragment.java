package com.bihe0832.readhub.module.readhub.news;

import com.bihe0832.readhub.framework.request.HttpResponseHandler;
import com.bihe0832.readhub.framework.request.RequestServer;
import com.bihe0832.readhub.libware.file.Logger;
import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.module.readhub.ReadhubFragment;
import com.bihe0832.readhub.module.readhub.news.request.GetNewsRequest;
import com.bihe0832.readhub.module.readhub.news.request.GetNewsResponse;
import com.bihe0832.readhub.module.readhub.news.request.NewsInfo;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;


public class NewsFragment extends ReadhubFragment {

    protected NewsAdapter mTopicAdapter;

    @Override
    protected void initView() {
        mTopicAdapter = new NewsAdapter(getMContext(), new ArrayList<NewsInfo>());
        super.initView();
    }
    @Override
    protected void getData() {
        GetNewsRequest request = new GetNewsRequest(mCursor, pageSize, new GetNewsResponseHandle());
        RequestServer.getInstance().doRequest(request);
    }

    private class GetNewsResponseHandle implements
            HttpResponseHandler<GetNewsResponse> {


        @Override
        public void onResponse(final GetNewsResponse response) {
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
