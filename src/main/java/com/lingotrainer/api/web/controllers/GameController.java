package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.web.mapper.GameFormMapper;
import com.lingotrainer.api.web.response.CreateGameResponse;
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
    private GameFormMapper gameFormMapper;

    public GameController(GameService gameService, GameFormMapper gameFormMapper) {
        this.gameService = gameService;
        this.gameFormMapper = gameFormMapper;
    }

    @PostMapping(produces = "application/json")
    @Authenticated
    public ResponseEntity<CreateGameResponse> startNewGame(@RequestParam("languageCode") String languageCode) {
        return ok(this.gameFormMapper.convertToResponse(gameService.createNewGame(languageCode)));
    }
}
