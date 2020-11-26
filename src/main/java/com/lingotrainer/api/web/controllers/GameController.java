package com.lingotrainer.api.web.controllers;

import com.lingotrainer.application.game.GameService;
import com.lingotrainer.api.annotation.Authenticated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
