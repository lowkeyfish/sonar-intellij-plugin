package com.yujunyang.intellij.plugin.sonar.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SonarApi {

    @GET("/api/qualityprofiles/search?defaults=true&language=java")
    Call<QualityProfilesSearchResponse> qualityProfilesSearch();

    @GET("/api/rules/search?languages=java&activation=true&statuses=READY&ps=500&f=repo,name,htmlDesc,params,severity")
    Call<RulesSearchResponse> rulesSearch(@Query("qprofile") String profileKey, @Query("p") int page);
}
