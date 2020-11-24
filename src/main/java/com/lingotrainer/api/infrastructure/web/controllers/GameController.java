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

/*    @PutMapping(path = "/{id}")
    @Authenticated
    public ResponseEntity<?> playTurn(@PathVariable("id") int gameId, @RequestParam("guessedWord") String guessedWord) {
        Game game = this.gameService.findById(gameId).orElseThrow(() -> new NotFoundException(String.format("Game ID %d could not be found.", gameId)));
        Round currentRound = this.roundService.findCurrentRound(gameId).orElseThrow(() -> new NotFoundException(String.format("Current round by game ID %d could not be found.", gameId)));
        Turn currentTurn = this.roundService.findCurrentTurn(currentRound).orElseThrow(() -> new NotFoundException("Current turn could not be found."));
        currentTurn.setGuessedWord(guessedWord);

        currentTurn.validateTurn(currentRound.getWord());

        if (Integer.parseInt(currentTurn.getFeedback().get("code").toString()) != -9999) {
            this.roundService.finishTurn(currentTurn);
            return ok(currentTurn);
        }

        boolean correctGuess = currentTurn.getGuessedWord().equalsIgnoreCase(currentRound.getWord());

        Turn newTurn = Turn.builder()
                .guessedWord(currentTurn.getGuessedWord())
                .correctGuess(correctGuess)
                .startedAt(Instant.now())
                .roundId(new RoundId(currentRound.getRoundId()))
                .build();

        if (correctGuess) {
            game.setScore(game.getScore() + 1);
            this.roundService.createNewRound(game);
            this.gameService.save(game);
        } else {
            this.roundService.finishTurn(currentTurn);
        }

        return ok(newTurn);
    }*/
}
