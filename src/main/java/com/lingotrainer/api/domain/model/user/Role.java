package com.lingotrainer.api.domain.model.user;

import lombok.Getter;

@Getter
public enum Role {
    TRAINEE("ROLE_TRAINEE"), ADMIN("ROLE_ADMIN");

    String value;

    Role(String value) {
        this.value = value;
    }
}
