/*
 * Copyright 2021 Yu Junyang
 * https://github.com/lowkeyfish
 *
 * This file is part of Sonar Intellij plugin.
 *
 * Sonar Intellij plugin is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Sonar Intellij plugin is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar Intellij plugin.
 * If not, see <http://www.gnu.org/licenses/>.
 */

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
