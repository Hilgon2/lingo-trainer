package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.request.CreateGameRequest;
import com.lingotrainer.api.web.response.CreateGameResponse;
import com.lingotrainer.domain.model.game.Game;

public class GameFormMapper {
    public CreateGameResponse convertToResponse(Game game) {
        return CreateGameResponse.builder()
                .gameId(game.getGameId())
                .build();
    }
    public Game convertToDomainEntity(CreateGameRequest createGameRequest) {
        return Game.builder()
                .language(createGameRequest.getLanguageCode())
                .build();
    }
}
