package com.example.timed.challenge.gitstatistics.remote.client;

import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepository;
import com.example.timed.challenge.gitstatistics.model.remote.repository.GitRepositoryEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class RemoteAPIClientImpl<T> implements RemoteAPIClient<T>{

    private static final Logger logger = LoggerFactory.getLogger(RemoteAPIClientImpl.class);

    @Override
    public Flux<T> makeAPICallList(String url, HttpMethod httpMethod, Map<String, String> headers, MultiValueMap<String, String> params, Class<T> responseClass) {
        Optional<WebClient.ResponseSpec> responseSpecOptional = makeResponseSpec(httpMethod, url, headers, params);
        if (!responseSpecOptional.isPresent()) return Flux.empty();

        return handleOnStatus(responseSpecOptional, url)
                .bodyToFlux(responseClass)
                .timeout(Duration.ofSeconds(10))
                .log("category", Level.ALL, SignalType.ON_ERROR, SignalType.ON_COMPLETE, SignalType.CANCEL, SignalType.REQUEST);
    }

    @Override
    public Mono<T> makeAPICall(String url, HttpMethod httpMethod, Map<String, String> headers, MultiValueMap<String, String> params, Class<T> responseClass) {
        Optional<WebClient.ResponseSpec> responseSpecOptional = makeResponseSpec(httpMethod, url, headers, params);
        if (!responseSpecOptional.isPresent()) return Mono.empty();

        return handleOnStatus(responseSpecOptional, url)
                .bodyToMono(responseClass)
                .timeout(Duration.ofSeconds(10))
                .log("category", Level.ALL, SignalType.ON_ERROR, SignalType.ON_COMPLETE, SignalType.CANCEL, SignalType.REQUEST);
    }

    private Optional<WebClient.ResponseSpec> makeResponseSpec(HttpMethod httpMethod, String uri, Map<String, String> headers, MultiValueMap<String, String> params) {

        WebClient webClient = buildWebClient(headers);

        Optional responseSpecOptional = Optional.empty();
        if(httpMethod == HttpMethod.GET) {
            logger.debug("Calling GET " + uri + "...");
            responseSpecOptional = Optional.of(webClient.get()
                    .uri(buildUri(uri, params))
                    .retrieve());
        } /*else if(httpMethod == HttpMethod.POST){
            logger.debug("Calling POST " + uri + "...");
            responseSpecOptional = Optional.of(webClient
                    .post()
                    .body(BodyInserters.fromObject(body))
                    .retrieve());
        } */else {
            logger.debug(httpMethod.name() + " is not supported yet...");
        }
        return responseSpecOptional;
    }

    private WebClient buildWebClient(Map<String, String> headers) {
        WebClient.Builder webClientBuilder = WebClient.builder();

        if(headers != null && !headers.isEmpty()) {

            for(Map.Entry<String, String> header : headers.entrySet()) {
                webClientBuilder = webClientBuilder.defaultHeader(header.getKey(), header.getValue()); }

        }
        return webClientBuilder.build();
    }

    private URI buildUri(String url, MultiValueMap<String, String> params) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(url);


        if(params != null && !params.isEmpty()) {
            uriComponentsBuilder.queryParams(params);
        }

        return uriComponentsBuilder.build().toUri();
    }

    private WebClient.ResponseSpec handleOnStatus(Optional<WebClient.ResponseSpec> responseSpecOptional, String uri) {
        return responseSpecOptional.get().onStatus(HttpStatus::is4xxClientError, clientResponse -> handle4xxError(clientResponse, uri))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> handle5xxError(clientResponse, uri));
    }

    private Mono<? extends Throwable> handle4xxError(ClientResponse clientResponse, String uri) {
        logger.error("Bad user input resulted from making a http call to the url " + uri + " Error was "
                + clientResponse.statusCode() + clientResponse.toEntity(String.class));
        return Mono.error(new Exception());
    }

    private Mono<? extends Throwable> handle5xxError(ClientResponse clientResponse, String uri) {
        logger.error("Encountered an interval server error while making a http call to the url " + uri + " Error was "
                + clientResponse.statusCode() + clientResponse.toEntity(String.class));
        return Mono.error(new Exception());
    }
}
