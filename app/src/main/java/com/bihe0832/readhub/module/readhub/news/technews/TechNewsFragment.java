package com.bihe0832.readhub.module.readhub.news.technews;

import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.libware.util.TimeUtils;
import com.bihe0832.readhub.module.readhub.ReadhubFragment;
import com.bihe0832.readhub.module.readhub.news.devnews.IDevNewsService;
import com.bihe0832.readhub.module.readhub.news.api.NewsAdapter;
import com.bihe0832.readhub.module.readhub.news.api.bean.News;
import com.bihe0832.readhub.module.readhub.news.api.bean.NewsRsp;
import com.bihe0832.readhub.network.ApiClient;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TechNewsFragment extends ReadhubFragment {

    protected NewsAdapter mNewsAdapter;

    @Override
    protected void initView() {
        mNewsAdapter = new NewsAdapter(getMContext(), new ArrayList<News>());
        super.initView();
    }
    @Override
    protected void getData() {
        ApiClient.create(ITechNewsService.class).requestNewsList(mCursor, pageSize).enqueue(new Callback<NewsRsp>() {
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

                mCursor = "" + TimeUtils.getTimeStampByReahubDateString(newsRsp.getData().get(newsRsp.getData().size() - 1).getPublishDate());
                pageSize = newsRsp.getPageSize();
                loadComplete();
            }

            @Override
            public void onFailure(Call<NewsRsp> call, Throwable t) {
                // TODO
            }
        });
    }

    @Override
    protected void clearAdapter() {
        mNewsAdapter.clear();
    }

    @Override
    protected void setAdapter(XRecyclerView recyclerView) {
        recyclerView.setAdapter(mNewsAdapter);
    }
}
