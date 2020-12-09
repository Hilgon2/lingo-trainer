package com.lingotrainer.domain.model.user;

import lombok.Getter;

@Getter
public enum Role {
    TRAINEE("ROLE_TRAINEE"), ADMIN("ROLE_ADMIN");

    private String value;

    Role(String value) {
        this.value = value;
    }
}
