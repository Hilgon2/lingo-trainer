package com.lingotrainer.api.controller;

import com.google.gson.Gson;
import com.lingotrainer.api.annotation.Authenticated;
import com.lingotrainer.api.exception.GeneralException;
import com.lingotrainer.api.exception.InvalidDataException;
import com.lingotrainer.api.exception.NotFoundException;
import com.lingotrainer.api.model.*;
import com.lingotrainer.api.security.component.AuthenticationFacade;
import com.lingotrainer.api.service.GameService;
import com.lingotrainer.api.service.TurnService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games")
public class GameController {

    private final AuthenticationFacade authenticationFacade;
    private GameService gameService;
    private TurnService turnService;

    public GameController(AuthenticationFacade authenticationFacade, GameService gameService, TurnService turnService) {
        this.authenticationFacade = authenticationFacade;
        this.gameService = gameService;
        this.turnService = turnService;
    }

    @PostMapping
    @Authenticated
    @ResponseBody
    public ResponseEntity<?> startNewGame(@RequestParam("languageCode") String languageCode) {
        if (!new File(String.format("src/main/resources/words/%s.json", languageCode)).exists()) {
            throw new NotFoundException(String.format("Language code '%s' not found.", languageCode));
        }

        String randomWord;
        try {
            Gson gson = new Gson();
            String targetFileReader = new String(Files.readAllBytes(Paths.get(String.format("src/main/resources/words/%s.json", languageCode))));
            List<String> words = gson.fromJson(targetFileReader, Word.class).getWords();

            randomWord = words.get(new Random().nextInt(words.size()));
        } catch (IOException e) {
            throw new GeneralException("Someting went wrong trying to read the language file");
        }

        Game game = gameService.createNewGame(Game.builder()
                .user(authenticationFacade.getUser())
                .word(randomWord)
                .language(languageCode)
                .gameStatus(Game.GameStatus.ACTIVE)
                .build());

        return ok(game);
    }

    @PutMapping(path = "/{id}")
    @Authenticated
    @ResponseBody
    public ResponseEntity<?> playGame(@PathVariable("id") int gameId, @RequestBody Turn turn) {
        Instant currentTime = Instant.now();
        Game game = gameService.findById(gameId).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", gameId)));
        List<GuessedLetter> guessedLetters = new ArrayList<>();

        if (turn.getGuessedWord() == null) {
            throw new NotFoundException("Turn has no guessedWord value");
        }

        if (game.getWord().length() != turn.getGuessedWord().length()) {
            throw new InvalidDataException("The words do not have the same length");
        }

        int index = 0;

        for (char letter : turn.getGuessedWord().toUpperCase().toCharArray()) {
            LetterFeedback letterFeedback;
            if (letter == game.getWord().charAt(index)) {
                letterFeedback = LetterFeedback.CORRECT;
            } else if (game.getWord().indexOf(letter) != -1) {
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

        List<Turn> turns = new ArrayList<>();
        Turn newTurn = Turn.builder()
                .guessedLetters(guessedLetters)
                .guessedWord(turn.getGuessedWord())
                .createdAt(currentTime)
                .game(game)
                .build();

        turns.add(newTurn);
        turnService.save(newTurn);
        game.setTurns(turns);
        game = gameService.save(game);
        System.out.println(game);


        return ok(game);
    }
}
