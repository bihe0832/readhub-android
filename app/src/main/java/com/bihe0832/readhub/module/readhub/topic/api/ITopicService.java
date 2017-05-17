package com.bihe0832.readhub.module.readhub.topic.api;

import com.bihe0832.readhub.module.readhub.topic.api.bean.TopicRsp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by erichua on 5/14/15.
 */
public interface ITopicService {
    @GET("topic")
    Call<TopicRsp> requestTopicList(@Query("lastCursor") String lastCursor, @Query("pageSize") int pageSize);
}
