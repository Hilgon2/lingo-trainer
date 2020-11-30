package com.lingotrainer.api.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class NewRoundResponse {
    private char firstLetter;
    private int lettersAmount;
}
