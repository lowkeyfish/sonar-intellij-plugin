package com.yujunyang.intellij.plugin.sonar.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yujunyang.intellij.plugin.sonar.common.exceptions.ApiRequestFailedException;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import retrofit2.Retrofit;

public class SonarApiImpl {
    private static SonarApi sonarApi;

    static {
        Retrofit retrofit = ApiUtils.createRetrofit(WorkspaceSettings.getInstance().sonarHostUrl);
        sonarApi = retrofit.create(SonarApi.class);
    }

    public String getJavaDefaultProfileKey() throws ApiRequestFailedException {
        try {
            QualityProfilesSearchResponse qualityProfilesSearchResponse = sonarApi.qualityProfilesSearch().execute().body();
            List<QualityProfilesSearchResponse.Profile> profiles = qualityProfilesSearchResponse.getProfiles();
            if (profiles == null || profiles.size() == 0) {
                throw new ApiRequestFailedException("The default profile for the Java language was not obtained");
            }
            QualityProfilesSearchResponse.Profile javaDefaultProfile = profiles.get(0);
            String profileKey = javaDefaultProfile.getKey();
            return profileKey;
        } catch (IOException e) {
            throw new ApiRequestFailedException("The default profile for the Java language search failed:" + e.getMessage(), e);
        }
    }

    public List<RulesSearchResponse.Rule> getJavaDefaultProfileRules() throws ApiRequestFailedException {
        String javaDefaultProfileKey = getJavaDefaultProfileKey();
        try {
            List<RulesSearchResponse.Rule> ret = new ArrayList<>();
            RulesSearchResponse rulesSearchResponse;
            int page = 1;
            do {
                rulesSearchResponse = sonarApi.rulesSearch(javaDefaultProfileKey, page).execute().body();
                ret.addAll(rulesSearchResponse.getRules());

                page++;
            } while (rulesSearchResponse.getTotal() > rulesSearchResponse.getPage() * rulesSearchResponse.getPageSize());

            return ret;
        } catch (IOException e) {
            throw new ApiRequestFailedException("The rules of default profile for the Java language search failed:" + e.getMessage(), e);
        }
    }
}
