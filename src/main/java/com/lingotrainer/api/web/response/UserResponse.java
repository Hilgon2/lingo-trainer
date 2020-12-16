package com.lingotrainer.api.web.response;

import lombok.*;

@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    private String username;
    private int highscore;
    private boolean admin;
}
