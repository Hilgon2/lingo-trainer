package com.lingotrainer.api.infrastructure.web.mapper;

import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.infrastructure.web.response.NewRoundResponse;

public class RoundFormMapper {

    public NewRoundResponse convertToForm(Round round) {
        return NewRoundResponse.builder()
                .roundId(round.getRoundId())
                .firstLetter(round.getWord().charAt(0))
                .lettersAmount(round.getWord().length())
                .build();
    }
}
