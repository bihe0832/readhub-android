package com.bihe0832.readhub.module.readhub.news;

import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.module.readhub.ReadhubFragment;
import com.bihe0832.readhub.module.readhub.news.api.INewsService;
import com.bihe0832.readhub.module.readhub.news.api.bean.News;
import com.bihe0832.readhub.module.readhub.news.api.bean.NewsRsp;
import com.bihe0832.readhub.network.ApiClient;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends ReadhubFragment {

    protected NewsAdapter mNewsAdapter;

    @Override
    protected void initView() {
        mNewsAdapter = new NewsAdapter(getMContext(), new ArrayList<News>());
        super.initView();
    }
    @Override
    protected void getData() {
//        GetNewsRequest request = new GetNewsRequest(mCursor, pageSize, new GetNewsResponseHandle());
//        RequestServer.getInstance().doRequest(request);

        ApiClient.create(INewsService.class).requestNewsList(mCursor, pageSize).enqueue(new Callback<NewsRsp>() {
            @Override
            public void onResponse(Call<NewsRsp> call, Response<NewsRsp> response) {
                final NewsRsp newsRsp = response.body();
                if (newsRsp == null || newsRsp.getData() == null) {
                    return;
                }

                if(null != newsRsp.getData() && newsRsp.getData().size() > 0 ){
                    ShakebaThreadManager.getInstance().runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mNewsAdapter.addAll(newsRsp.getData());
                        }
                    });
                }

                String dateTime = newsRsp.getData().get(newsRsp.getData().size() - 1).getPublishDate();
                mCursor = "" + new Date(dateTime).getTime();
                pageSize = newsRsp.getPageSize();
                loadComplete();
            }

            @Override
            public void onFailure(Call<NewsRsp> call, Throwable t) {

            }
        });
    }

//    private class GetNewsResponseHandle implements
//            HttpResponseHandler<GetNewsResponse> {
//
//
//        @Override
//        public void onResponse(final GetNewsResponse response) {
//            Logger.d(response.toString());
//            if(null != response.mInfoList && response.mInfoList.size() > 0 ){
//                ShakebaThreadManager.getInstance().runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mNewsAdapter.addAll(response.mInfoList);
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
        mNewsAdapter.clear();
    }

    @Override
    protected void setAdapter(XRecyclerView recyclerView) {
        recyclerView.setAdapter(mNewsAdapter);
    }
}
