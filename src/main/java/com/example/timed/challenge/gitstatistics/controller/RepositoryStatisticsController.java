package com.example.timed.challenge.gitstatistics.controller;

import com.example.timed.challenge.gitstatistics.model.RepositoryStatistic;
import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepositoryEnvelope;
import com.example.timed.challenge.gitstatistics.service.RepositoryStatisticsFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/repoStatistics")
public class RepositoryStatisticsController {

    @Autowired
    private  RepositoryStatisticsFacadeService repositoryStatisticsFacadeService;

   @GetMapping
    public Flux<RepositoryStatistic> getRepoStatistics(@RequestParam Integer numRepos, @RequestParam Integer numCommits) {
         return repositoryStatisticsFacadeService.getRepositoryStatistics(numRepos, numCommits);
    }

    @GetMapping("/repos")
    public Flux<RepositoryStatistic> getRepoStatistics() {
        Flux<RepositoryStatistic> gitRepositoryEnvelopeFlux = repositoryStatisticsFacadeService.getRepositories();
        return gitRepositoryEnvelopeFlux;
    }

}
