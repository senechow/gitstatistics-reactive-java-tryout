package com.example.timed.challenge.gitstatistics.remote.service;

import com.example.timed.challenge.gitstatistics.config.GitRemoteConfig;
import com.example.timed.challenge.gitstatistics.model.remote.GitContributorStatistics;
import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepositoryEnvelope;
import com.example.timed.challenge.gitstatistics.model.remote.user.GitUser;
import com.example.timed.challenge.gitstatistics.remote.client.RemoteAPIClient;
import com.example.timed.challenge.gitstatistics.remote.client.RemoteAPIClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class GitRemoteService {

    @Autowired
    private GitRemoteConfig gitRemoteConfig;

    public Mono<GitRepositoryEnvelope> getRepos() {
        RemoteAPIClient<GitRepositoryEnvelope> remoteAPIClient = new RemoteAPIClientImpl<>();
        return remoteAPIClient.test();
    }

    public Mono<GitRepositoryEnvelope> getGitRepositories(int numRepos) {

        String url = gitRemoteConfig.getBaseUrl() + gitRemoteConfig.getEndpoints().getSearchRepositories();
        Map<String, String> headers = buildHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("q", "language:clojure");
        params.add("sort", "stars");
        params.add("per_page", numRepos + "");

        RemoteAPIClient<GitRepositoryEnvelope> remoteAPIClient = new RemoteAPIClientImpl<>();
        return remoteAPIClient.makeAPICall(url, HttpMethod.GET, headers, params, GitRepositoryEnvelope.class);

    }

    public Flux<GitContributorStatistics> getGitRepositoryContributorStatistics(String repoName, String ownerUserName, int numCommits) {

        String url = gitRemoteConfig.getBaseUrl() + gitRemoteConfig.getEndpoints().getRepositoryContributorStats().replace("{owner}", ownerUserName).replace("{repo}", repoName);
        Map<String, String> headers = buildHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("per_page", numCommits + "");

        RemoteAPIClient<GitContributorStatistics> remoteAPIClient = new RemoteAPIClientImpl<>();
        return remoteAPIClient.makeAPICallList(url, HttpMethod.GET, headers, params, GitContributorStatistics.class).take(numCommits);

    }

    public Mono<GitUser> getGitUser(String userName) {

        String url = gitRemoteConfig.getBaseUrl() + gitRemoteConfig.getEndpoints().getUsers().replace("{username}", userName);
        Map<String, String> headers = buildHeaders();

        RemoteAPIClient<GitUser> remoteAPIClient = new RemoteAPIClientImpl<>();
        return remoteAPIClient.makeAPICall(url, HttpMethod.GET, headers, null, GitUser.class);
    }

    private Map<String, String> buildHeaders() {

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", gitRemoteConfig.getAcceptVersionValue());
        headers.put("authorization", "token " + gitRemoteConfig.getAccessToken());
        headers.put(gitRemoteConfig.getUserAgentHeader(), "");

        return headers;

    }


}
