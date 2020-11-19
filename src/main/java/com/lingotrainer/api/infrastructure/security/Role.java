package com.lingotrainer.api.infrastructure.security;

import lombok.Getter;

@Getter
public enum Role {
    TRAINEE("trainee"), ADMIN("admin");

    String value;

    Role(String value) {
        this.value = value;
    }
}
