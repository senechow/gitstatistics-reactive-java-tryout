package com.example.timed.challenge.gitstatistics.model.remote.repository;

import com.example.timed.challenge.gitstatistics.model.remote.user.GitUserSummary;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GitRepository {

    private Long id;
    private String full_name;
    private GitUserSummary owner;
    private Integer stargazers_count;
    private String name;

}
