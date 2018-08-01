package com.example.timed.challenge.gitstatistics.model.remote.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GitUser extends GitUserSummary {

    private String email;

    private GitUser(String login, Long id, String email) {
        super(login, id);
        this.email = email;
    }

}
