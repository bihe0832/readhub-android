package com.bihe0832.readhub.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @desc Created by erichua on 15/05/2017.
 */
public class ApiClient {
    private static String baseUrl = "";
    private static Retrofit retrofit = null;

    public static void init(String baseUrl) {
        setBaseUrl(baseUrl);
//
//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
//                .tlsVersions(TlsVersion.TLS_1_2)
//                .cipherSuites(
//                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
//                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
//                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
//                .build();

//        URL url = null;
//        NoSSLv3SocketFactory sslFactory = null;
//        try {
//            url = new URL(ApiClient.baseUrl);
//            sslFactory = new NoSSLv3SocketFactory();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//                .connectionSpecs(Collections.singletonList(spec))
//                .sslSocketFactory(sslFactory)
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().create();

        ApiClient.retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    public static <T> T create(final Class<T> serviceCls) {
        if (ApiClient.retrofit == null) {
            throw new IllegalStateException("ApiClient.retrofit is null, your should call ApiClient.init it before use");
        }
        return retrofit.create(serviceCls);
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        ApiClient.baseUrl = baseUrl;
    }
}
