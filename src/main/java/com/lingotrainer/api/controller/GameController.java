package com.lingotrainer.api.controller;

import com.lingotrainer.api.annotation.Authenticated;
import com.lingotrainer.api.exception.NotFoundException;
import com.lingotrainer.api.model.*;
import com.lingotrainer.api.security.component.AuthenticationFacade;
import com.lingotrainer.api.service.GameService;
import com.lingotrainer.api.service.RoundService;
import com.lingotrainer.api.service.TurnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Instant;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games")
public class GameController {

    private final AuthenticationFacade authenticationFacade;
    private GameService gameService;
    private TurnService turnService;
    private RoundService roundService;

    public GameController(AuthenticationFacade authenticationFacade, GameService gameService, TurnService turnService, RoundService roundService) {
        this.authenticationFacade = authenticationFacade;
        this.gameService = gameService;
        this.turnService = turnService;
        this.roundService = roundService;
    }

    @PostMapping(produces = "application/json")
    @Authenticated
    @ResponseBody
    public ResponseEntity<?> startNewGame(@RequestParam("languageCode") String languageCode) {
        if (!new File(String.format("src/main/resources/dictionary/%s.json", languageCode)).exists()) {
            throw new NotFoundException(String.format("Language code '%s' not found.", languageCode));
        }

        Game game = Game.builder()
                .user(authenticationFacade.getUser())
                .language(languageCode)
                .gameStatus(Game.GameStatus.ACTIVE)
                .build();

        game = gameService.createNewGame(game);

        this.roundService.createNewRound(game);

        return ok(game);
    }

    @PutMapping(path = "/{id}")
    @Authenticated
    @ResponseBody
    public ResponseEntity<?> playTurn(@PathVariable("id") int gameId, @RequestParam("guessedWord") String guessedWord) {
        Game game = this.gameService.findById(gameId).orElseThrow(() -> new NotFoundException(String.format("Game ID %d could not be found.", gameId)));
        Round currentRound = this.roundService.findCurrentRound(gameId).orElseThrow(() -> new NotFoundException(String.format("Current round by game ID %d could not be found.", gameId)));
        Turn currentTurn = this.turnService.findCurrentTurn(currentRound).orElseThrow(() -> new NotFoundException("Current turn could not be found."));
        currentTurn.setGuessedWord(guessedWord);

        currentTurn.validateTurn();

        // Trim and capitalize the guessed word. A null does not have a trim or toUpperCase method. This could otherwise possibly cause an error.
        currentTurn.setGuessedWord(guessedWord.toUpperCase().trim());

        if (Integer.parseInt(currentTurn.getFeedback().get("code").toString()) != -9999) {
            this.turnService.finishTurn(currentTurn);
            return ok(currentTurn);
        }

        int index = 0;
        List<GuessedLetter> guessedLetters = new ArrayList<>();

        for (char letter : currentTurn.getGuessedWord().toCharArray()) {
            LetterFeedback letterFeedback;
            if (letter == Character.toUpperCase(currentRound.getWord().charAt(index))) {
                letterFeedback = LetterFeedback.CORRECT;
            } else if (currentRound.getWord().toUpperCase().indexOf(letter) != -1) {
                letterFeedback = LetterFeedback.PRESENT;
            } else {
                letterFeedback = LetterFeedback.ABSENT;
            }
            guessedLetters.add(GuessedLetter.builder()
                    .letter(letter)
                    .letterFeedback(letterFeedback)
                    .build());
            index++;
        }

        boolean correctGuess = currentTurn.getGuessedWord().equalsIgnoreCase(currentRound.getWord());

        Turn newTurn = Turn.builder()
                .guessedWord(currentTurn.getGuessedWord())
                .guessedLetters(guessedLetters)
                .correctGuess(correctGuess)
                .startedAt(Instant.now())
                .round(currentRound)
                .build();

        if (correctGuess) {
            game.setScore(game.getScore() + 1);
            this.roundService.createNewRound(game);
            this.gameService.save(game);
        } else {
            this.turnService.finishTurn(currentTurn);
        }

        return ok(newTurn);
    }
}
