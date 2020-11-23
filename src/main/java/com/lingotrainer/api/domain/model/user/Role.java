package com.lingotrainer.api.domain.model.user;

import lombok.Getter;

@Getter
public enum Role {
    TRAINEE("trainee"), ADMIN("admin");

    String value;

    Role(String value) {
        this.value = value;
    }
}
