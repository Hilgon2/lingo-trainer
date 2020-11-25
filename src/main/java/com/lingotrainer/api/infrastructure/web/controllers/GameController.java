package com.lingotrainer.api.infrastructure.web.controllers;

import com.lingotrainer.api.application.game.GameService;
import com.lingotrainer.api.util.annotation.Authenticated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games")
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(produces = "application/json")
    @Authenticated
    public ResponseEntity<Integer> startNewGame(@RequestParam("languageCode") String languageCode) {
        int newGameId = gameService.createNewGame(languageCode);

        return ok(newGameId);
    }
}
