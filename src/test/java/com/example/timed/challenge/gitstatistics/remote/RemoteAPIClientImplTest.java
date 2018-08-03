package com.example.timed.challenge.gitstatistics.remote;

import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepository;
import com.example.timed.challenge.gitstatistics.remote.service.GitRemoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteAPIClientImplTest {

    @Autowired
    private GitRemoteService gitRemoteService;

    @Test
    public void test_GetGitRepos() {

        Flux<GitRepository> gitRepositoryFlux = gitRemoteService.getGitRepositories(1)
                .map(gitRepositoryEnvelope -> gitRepositoryEnvelope.getItems())
                .flatMapMany(Flux::fromIterable)
                ;

        StepVerifier.create(gitRepositoryFlux)
                .expectNextMatches(t -> t.getId() != null && t.getStargazers_count() != null)
                .verifyComplete()
                ;

    }

}
