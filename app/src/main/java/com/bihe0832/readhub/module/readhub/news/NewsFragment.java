package com.bihe0832.readhub.module.readhub.news;

import com.bihe0832.readhub.libware.thread.ShakebaThreadManager;
import com.bihe0832.readhub.module.readhub.ReadhubFragment;
import com.bihe0832.readhub.module.readhub.news.api.INewsService;
import com.bihe0832.readhub.module.readhub.news.api.bean.News;
import com.bihe0832.readhub.module.readhub.news.api.bean.NewsRsp;
import com.bihe0832.readhub.network.ApiClient;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    mCursor = "" + format.parse(dateTime).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
