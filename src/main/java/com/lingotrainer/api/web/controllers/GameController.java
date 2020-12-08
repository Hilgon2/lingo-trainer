package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.web.mapper.GameFormMapper;
import com.lingotrainer.api.web.mapper.RoundFormMapper;
import com.lingotrainer.api.web.mapper.TurnFormMapper;
import com.lingotrainer.api.web.request.CreateGameRequest;
import com.lingotrainer.api.web.request.PlayTurnRequest;
import com.lingotrainer.api.web.response.GameResponse;
import com.lingotrainer.api.web.response.NewRoundResponse;
import com.lingotrainer.api.web.response.PlayTurnResponse;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.game.GameService;
import com.lingotrainer.api.annotation.Authenticated;
import com.lingotrainer.application.game.round.RoundService;
import com.lingotrainer.application.game.round.turn.TurnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games")
public class GameController {

    private GameService gameService;
    private AuthenticationService authenticationService;
    private RoundService roundService;
    private TurnService turnService;
    private GameFormMapper gameFormMapper;
    private RoundFormMapper roundFormMapper;
    private TurnFormMapper turnFormMapper;

    public GameController(GameService gameService,
                          AuthenticationService authenticationService,
                          RoundService roundService, TurnService turnService,
                          GameFormMapper gameFormMapper,
                          RoundFormMapper roundFormMapper,
                          TurnFormMapper turnFormMapper) {
        this.gameService = gameService;
        this.authenticationService = authenticationService;
        this.roundService = roundService;
        this.turnService = turnService;
        this.gameFormMapper = gameFormMapper;
        this.roundFormMapper = roundFormMapper;
        this.turnFormMapper = turnFormMapper;
    }

    @PostMapping(produces = "application/json")
    @Authenticated
    public ResponseEntity<GameResponse> startNewGame(@RequestBody CreateGameRequest createGameRequest) {
        return ok(this.gameFormMapper.convertToResponse(
                gameService.createNewGame(createGameRequest.getLanguageCode()))
        );
    }

    @GetMapping(path = "/active")
    @Authenticated
    public ResponseEntity<GameResponse> findActiveGame() {
        return ok(this.gameFormMapper.convertToResponse(
                this.gameService.findActiveGame(this.authenticationService.getUser().getUserId())));
    }

    @PostMapping(produces = "application/json", path = "/{gameId}/rounds")
    @Authenticated
    public ResponseEntity<NewRoundResponse> createNewRound(@PathVariable int gameId) {
        NewRoundResponse newRoundResponse = this.roundFormMapper.convertToResponse(roundService.createNewRound(gameId));
        return ok(newRoundResponse);
    }

    @PostMapping(produces = "application/json", path = "/{gameId}/rounds/turn")
    @Authenticated
    public ResponseEntity<PlayTurnResponse> playTurn(@PathVariable int gameId,
                                                     @RequestBody PlayTurnRequest playTurnRequest) {
        return ok(this.turnFormMapper.convertToResponse(
                this.turnService.playTurn(gameId, playTurnRequest.getGuessedWord()))
        );
    }
}
