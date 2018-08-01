package com.example.timed.challenge.gitstatistics.model.remote;

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
public class GitContributorStatistics {

    private GitUserSummary author;
    private Integer total;

}
