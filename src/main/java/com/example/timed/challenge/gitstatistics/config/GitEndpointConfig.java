package com.example.timed.challenge.gitstatistics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

public class GitEndpointConfig {

    private String searchRepositories;
    private String repositoryContributorStats;
    private String users;

    public String getSearchRepositories() {
        return searchRepositories;
    }

    public void setSearchRepositories(String searchRepositories) {
        this.searchRepositories = searchRepositories;
    }

    public String getRepositoryContributorStats() {
        return repositoryContributorStats;
    }

    public void setRepositoryContributorStats(String repositoryContributorStats) {
        this.repositoryContributorStats = repositoryContributorStats;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
