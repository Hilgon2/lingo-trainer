package com.lingotrainer.api.web.mapper;

import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.api.web.response.NewRoundResponse;

public class RoundFormMapper {

    public NewRoundResponse convertToResponse(Round round) {
        return NewRoundResponse.builder()
                .roundId(round.getRoundId())
                .firstLetter(round.getWord().charAt(0))
                .lettersAmount(round.getWord().length())
                .build();
    }
}
