package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.request.CreateGameRequest;
import com.lingotrainer.api.web.response.GameResponse;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameId;

public class GameFormMapper {
    public GameResponse convertToResponse(Game game) {
        int gameId = game == null ? -1 : game.getGameId();
        return GameResponse.builder()
                .gameId(gameId)
                .build();
    }
    public Game convertToDomainEntity(CreateGameRequest createGameRequest) {
        return Game.builder()
                .language(createGameRequest.getLanguageCode())
                .build();
    }
}
