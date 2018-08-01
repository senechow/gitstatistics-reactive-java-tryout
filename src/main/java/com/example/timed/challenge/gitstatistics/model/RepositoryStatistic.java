package com.example.timed.challenge.gitstatistics.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RepositoryStatistic {

    private String repository_name;
    private Integer stargazers_count;
    private String owner;
    private List<Author> authors;
    private Flux<Author> authorFlux;
}
