package com.lingotrainer.api.infrastructure.web.controllers;

import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.GameId;
import com.lingotrainer.api.domain.model.game.GameStatus;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.application.game.GameService;
import com.lingotrainer.api.domain.model.game.round.RoundId;
import com.lingotrainer.api.domain.model.user.User;
import com.lingotrainer.api.domain.model.user.UserId;
import com.lingotrainer.api.util.annotation.Authenticated;
import com.lingotrainer.api.util.exception.NotFoundException;
import com.lingotrainer.api.application.authentication.AuthenticationService;
import com.lingotrainer.api.application.game.round.RoundService;
import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Instant;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games")
public class GameController {

    private final AuthenticationService authenticationService;
    private GameService gameService;
    private RoundService roundService;

    public GameController(AuthenticationService authenticationService, GameService gameService, RoundService roundService) {
        this.authenticationService = authenticationService;
        this.gameService = gameService;
        this.roundService = roundService;
    }

    @PostMapping(produces = "application/json")
    @Authenticated
    public ResponseEntity<?> startNewGame(@RequestParam("languageCode") String languageCode) {
        if (!new File(String.format("src/main/resources/dictionary/%s.json", languageCode)).exists()) {
            throw new NotFoundException(String.format("Language code '%s' not found.", languageCode));
        }

        User user = this.authenticationService.getUser();

        Game game = Game.builder()
                .userId(new UserId(user.getUserId()))
                .language(languageCode)
                .gameStatus(GameStatus.ACTIVE)
                .build();

        int newGameId = gameService.createNewGame(game);

        return ok(newGameId);
    }
}
