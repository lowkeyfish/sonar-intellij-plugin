package com.yujunyang.intellij.plugin.sonar.api;

import java.util.List;

public class QualityProfilesSearchResponse {
    private List<Profile> profiles;

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public static class Profile {
        private String key;
        private String language;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}

