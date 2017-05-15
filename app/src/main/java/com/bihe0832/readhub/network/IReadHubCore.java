package com.bihe0832.readhub.network;

import okhttp3.Request;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 *
 * Created by erichua on 5/14/15.
 */
public interface IReadHubCore {
    @GET("/pmbank-vq/version/upgrade")
    void reqCheckUpdate(@Body Request body);
}
