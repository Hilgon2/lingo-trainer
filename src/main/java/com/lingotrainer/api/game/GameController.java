package com.lingotrainer.api.game;

import com.lingotrainer.api.game.Game;
import com.lingotrainer.api.round.Round;
import com.lingotrainer.api.shared.annotation.Authenticated;
import com.lingotrainer.api.shared.exception.NotFoundException;
import com.lingotrainer.api.authentication.AuthenticationService;
import com.lingotrainer.api.game.GameService;
import com.lingotrainer.api.round.RoundService;
import com.lingotrainer.api.turn.GuessedLetter;
import com.lingotrainer.api.turn.LetterFeedback;
import com.lingotrainer.api.turn.Turn;
import com.lingotrainer.api.turn.TurnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Instant;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games")
public class GameController {

    private final AuthenticationService authenticationService;
    private GameService gameService;
    private TurnService turnService;
    private RoundService roundService;

    public GameController(AuthenticationService authenticationService, GameService gameService, TurnService turnService, RoundService roundService) {
        this.authenticationService = authenticationService;
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
                .user(authenticationService.getUser())
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
