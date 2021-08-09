package com.yujunyang.intellij.plugin.sonar.api;

import com.yujunyang.intellij.plugin.sonar.common.DefaultObjectMapper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class ApiUtils {
    public static Retrofit createRetrofit(String baseUrl) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(DefaultObjectMapper.getObjectMapper()))
                .baseUrl(baseUrl);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit;
    }

    public static Retrofit createRetrofit(String baseUrl, OkHttpClient client) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(DefaultObjectMapper.getObjectMapper()))
                .baseUrl(baseUrl)
                .client(client);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit;
    }
}
