package com.yujunyang.intellij.plugin.sonar.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RulesSearchResponse {
    private int total;
    @JsonProperty("p")
    private int page;
    @JsonProperty("ps")
    private int pageSize;
    private List<Rule> rules;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public static class Rule {
        private String htmlDesc;
        private String key;
        private String name;
        private String repo;
        private String severity;
        private String type;

        public String getHtmlDesc() {
            return htmlDesc;
        }

        public void setHtmlDesc(String htmlDesc) {
            this.htmlDesc = htmlDesc;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRepo() {
            return repo;
        }

        public void setRepo(String repo) {
            this.repo = repo;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
