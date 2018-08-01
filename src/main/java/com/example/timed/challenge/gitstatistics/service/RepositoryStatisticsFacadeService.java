package com.example.timed.challenge.gitstatistics.service;

import com.example.timed.challenge.gitstatistics.model.Author;
import com.example.timed.challenge.gitstatistics.model.RepositoryStatistic;
import com.example.timed.challenge.gitstatistics.model.remote.GitContributorStatistics;
import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepository;
import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepositoryEnvelope;
import com.example.timed.challenge.gitstatistics.model.remote.user.GitUser;
import com.example.timed.challenge.gitstatistics.remote.service.GitRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RepositoryStatisticsFacadeService {

    @Autowired
    private GitRemoteService gitRemoteService;

    public Flux<RepositoryStatistic> getRepositoryStatistics(int numRepos, int numCommits) {

        Mono<GitRepositoryEnvelope> gitRepositoryEnvelopeMono = gitRemoteService.getGitRepositories(numRepos);
        Flux<GitRepository> gitRepositoryFlux = gitRepositoryEnvelopeMono
                .map(gitRepositoryEnvelope -> gitRepositoryEnvelope.getItems())
                .flatMapMany(Flux::fromIterable);

        Flux<RepositoryStatistic> repositoryStatisticFlux = gitRepositoryFlux.map(gitRepository ->
            gitRemoteService.getGitRepositoryContributorStatistics(gitRepository.getName(), gitRepository.getOwner().getLogin(), numCommits))
                .zipWith(gitRepositoryFlux, ((gitContributorStatistics, gitRepository) -> {
                    return RepositoryStatistic.builder()
                            .repository_name(gitRepository.getFull_name())
                            .stargazers_count(gitRepository.getStargazers_count())
                            .authorFlux(buildAuthors(gitContributorStatistics))
                            .build();
                }
            ));

        return repositoryStatisticFlux
                .flatMap((repositoryStatistic -> {

                    Mono<RepositoryStatistic> repositoryStatisticMono = Mono.just(repositoryStatistic);
                    Flux<Author> authorsFlux = repositoryStatistic.getAuthorFlux()
                        .flatMap(author -> {
                            Flux<Author> authorFlux = Flux.just(author);
                            Mono<GitUser> userMono = gitRemoteService.getGitUser(author.getUsername());
                            return Flux.zip(authorFlux, userMono, (auth, gitUser) -> Author.builder().email(gitUser.getEmail() != null ? gitUser.getEmail() : gitUser.getLogin())
                                    .number_of_commits(auth.getNumber_of_commits())
                                    .build());

                        }).sort(Comparator.comparing(Author::getNumber_of_commits)
                            .thenComparing(Author::getEmail)
                            .reversed());

                   return Flux.zip(repositoryStatisticMono, authorsFlux.collectList(), (repoStats, authorList) ->
                          RepositoryStatistic.builder()
                                .repository_name(repoStats.getRepository_name())
                                .stargazers_count(repoStats.getStargazers_count())
                                .authors(authorList)
                                .build());

                })).sort((Comparator.comparing(RepositoryStatistic::getStargazers_count)
                                        .thenComparing(RepositoryStatistic::getRepository_name))
                                        .reversed());

    }

    public Flux<RepositoryStatistic> getRepositories() {

        Map<GitRepository, List<GitContributorStatistics>> gitRepositoryListMap = new LinkedHashMap<>();

        Mono<GitRepositoryEnvelope> gitRepositoryEnvelopeMono = gitRemoteService.getGitRepositories(1);
        Flux<GitRepository> gitRepositoryFlux = gitRepositoryEnvelopeMono
                .map(gitRepositoryEnvelope -> gitRepositoryEnvelope.getItems())
                .flatMapMany(Flux::fromIterable);

        return gitRepositoryFlux.map(gitRepository ->
                gitRemoteService.getGitRepositoryContributorStatistics(gitRepository.getFull_name(), gitRepository.getOwner().getLogin(), 1))
                .zipWith(gitRepositoryFlux, ((gitContributorStatistics, gitRepository) -> RepositoryStatistic.builder()
                        .repository_name(gitRepository.getFull_name())
                        .build()));

    }

    private Flux<Author> buildAuthors(Flux<GitContributorStatistics> gitContributorStatistics) {

        return gitContributorStatistics
                .map(gitContributorStatistic -> Author.builder()
                        .number_of_commits(gitContributorStatistic.getTotal())
                        .username(gitContributorStatistic.getAuthor().getLogin())
                        .build());

    }


}
