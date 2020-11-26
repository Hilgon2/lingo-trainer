package com.lingotrainer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WordLength {
    FIVE(5),
    SIX(6),
    SEVEN(7);

    @Getter
    private int length;
}