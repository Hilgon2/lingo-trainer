package com.lingotrainer.api.web.mapper;

import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.api.web.response.RoundResponse;

public class RoundFormMapper {

    public RoundResponse convertToResponse(Round round) {
        if (round == null) {
            return RoundResponse.builder()
                    .active(false)
                    .build();
        }

        return RoundResponse.builder()
                .firstLetter(round.getWord().charAt(0))
                .lettersAmount(round.getWord().length())
                .active(round.isActive())
                .build();
    }
}
