package com.lingotrainer.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    private String username;
    private int highscore;
}
