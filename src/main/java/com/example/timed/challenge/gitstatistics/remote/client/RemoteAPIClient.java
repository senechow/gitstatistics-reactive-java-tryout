package com.example.timed.challenge.gitstatistics.remote.client;

import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepository;
import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepositoryEnvelope;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface RemoteAPIClient<T> {

    Flux<T> makeAPICallList(String url, HttpMethod httpMethod, Map<String, String> headers, MultiValueMap<String, String> params, Class<T> responseClass);

    Mono<T> makeAPICall(String url, HttpMethod httpMethod, Map<String, String> headers, MultiValueMap<String, String> params, Class<T> responseClass);

}
