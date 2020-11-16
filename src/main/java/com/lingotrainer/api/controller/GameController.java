package com.lingotrainer.api.controller;

import com.google.gson.Gson;
import com.lingotrainer.api.annotation.Authenticated;
import com.lingotrainer.api.exception.GameException;
import com.lingotrainer.api.exception.NotFoundException;
import com.lingotrainer.api.model.*;
import com.lingotrainer.api.security.component.AuthenticationFacade;
import com.lingotrainer.api.service.GameService;
import com.lingotrainer.api.service.RoundService;
import com.lingotrainer.api.service.TurnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
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
        if (!new File(String.format("src/main/resources/words/%s.json", languageCode)).exists()) {
            throw new NotFoundException(String.format("Language code '%s' not found.", languageCode));
        }

        Game game = Game.builder()
                .user(authenticationFacade.getUser())
                .language(languageCode)
                .gameStatus(Game.GameStatus.ACTIVE)
                .build();

        game = gameService.createNewGame(game);

        createNewRound(game);

        return ok(game);
    }

    @PutMapping(path = "/{id}")
    @Authenticated
    @ResponseBody
    public ResponseEntity<?> playTurn(@PathVariable("id") int gameId, @RequestParam("guessedWord") String guessedWord) {
        Game game = this.gameService.findById(gameId).orElseThrow(() -> new NotFoundException(String.format("Game ID %d could not be found.", gameId)));
        Round currentRound = roundService.findCurrentRound(gameId).orElseThrow(() -> new NotFoundException(String.format("Current round by game ID %d could not be found.", gameId)));
        Turn currentTurn = this.turnService.findCurrentTurn(currentRound).orElseThrow(() -> new NotFoundException("Current turn could not be found."));
        currentTurn.setGuessedWord(guessedWord);

        validateTurn(currentTurn, guessedWord);

        // Set guessedWord after null check. A null does not have a trim or toUpperCase method. This would otherwise cause an error.
        currentTurn.setGuessedWord(guessedWord.toUpperCase().trim());

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
            createNewRound(game);
            gameService.save(game);
        } else {
            finishTurn(currentTurn);
        }

        return ok(newTurn);
    }

    private void finishTurn(Turn currentTurn) {
        turnService.save(currentTurn);

        if (currentTurn.getRound().getTurns()
                .stream()
                .filter(t -> t.getGuessedWord() != null)
                .count() >= 5) {
            Game game = currentTurn.getRound().getGame();
            game.setGameStatus(Game.GameStatus.FINISHED);
            gameService.save(game);
            throw new GameException("Game over. You did not get the correct word in 5 turns");
        }

        if (currentTurn.getRound().getTurns().size() == 5) {
            createNewRound(currentTurn.getRound().getGame());
        } else {
            Instant currentTime = Instant.now();
            Turn newTurn = Turn.builder()
                    .startedAt(currentTime)
                    .round(currentTurn.getRound())
                    .build();

            turnService.save(newTurn);
        }
    }

    private void createNewRound(Game game) {
        String randomWord = getRandomWord(game.getLanguage());

        if (randomWord.length() < 5 || randomWord.length() > 7) {
            randomWord = getRandomWord(game.getLanguage());
        }

        Round round = Round.builder()
                .word(randomWord)
                .game(game)
                .turns(Collections.singletonList(Turn.builder()
                        .startedAt(Instant.now())
                        .build()))
                .build();

        round = roundService.save(round);
        Turn newTurn = round.getTurns().get(0);
        newTurn.setRound(round);
        this.turnService.save(newTurn);
    }

    private String getRandomWord(String languageCode) {
        String randomWord;
        try {
            Gson gson = new Gson();
            String targetFileReader = new String(Files.readAllBytes(Paths.get(String.format("src/main/resources/words/%s.json", languageCode))));
            List<String> words = gson.fromJson(targetFileReader, Word.class).getWords();

            randomWord = words.get(new Random().nextInt(words.size()));
        } catch (IOException e) {
            throw new NotFoundException("Someting went wrong trying to read the language file. The file might not exist.");
        }

        return randomWord;
    }

    private void validateTurn(Turn turn, String guessedWord) {
        Round round = turn.getRound();
        Game game = round.getGame();

        if (game.getGameStatus() != Game.GameStatus.ACTIVE) {
            throw new GameException("This game is not active. Please start a new game.");
        }

        if (guessedWord == null) {
            turn.setGuessedWord("-");
            finishTurn(turn);
            throw new NotFoundException("Turn has no guessed word value");
        }

        if (turn.getRound().getWord().length() != turn.getGuessedWord().length()) {
            finishTurn(turn);
            throw new GameException("The words do not have the same length");
        }

        if (!turn.getGuessedWord().chars().allMatch(Character::isLetter)) {
            finishTurn(turn);
            throw new GameException("The word can only contain alphabetical characters");
        }
        if (turn.getRound().getTurns()
                .stream()
                .filter(t -> t.getGuessedWord() != null)
                .count() >= 5) {
            createNewRound(game);
            throw new GameException("5 turns have already been played for this round.");
        }

        if (Duration.between(turn.getStartedAt(), Instant.now()).getSeconds() > 1500) { // TODO: change 1500 to 10 (seconds)
            finishTurn(turn);
            throw new GameException("Your turn took longer than 10 seconds.");
        }
    }
}
