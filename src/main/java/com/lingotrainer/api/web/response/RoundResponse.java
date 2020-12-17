package com.lingotrainer.api.web.response;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@Builder
@EqualsAndHashCode
public class RoundResponse {
    private char firstLetter;
    private int lettersAmount;
    private boolean active;
}
