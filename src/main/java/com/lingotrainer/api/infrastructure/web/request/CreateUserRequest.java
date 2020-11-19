package com.lingotrainer.api.infrastructure.web.request;

import com.lingotrainer.api.infrastructure.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest implements Serializable {
    private String username;
    private String password;
    private Role role;
}
