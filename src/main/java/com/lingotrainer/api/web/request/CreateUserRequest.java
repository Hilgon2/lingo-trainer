package com.lingotrainer.api.web.request;

import com.lingotrainer.domain.model.user.Role;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest implements Serializable {
    private String username;
    private String password;
    private Role role;
    private boolean active;
}
