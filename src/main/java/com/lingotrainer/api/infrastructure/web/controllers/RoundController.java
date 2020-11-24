package com.lingotrainer.api.infrastructure.web.controllers;

import com.lingotrainer.api.application.authentication.AuthenticationService;
import com.lingotrainer.api.application.game.GameService;
import com.lingotrainer.api.application.game.round.RoundService;
import com.lingotrainer.api.util.annotation.Authenticated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games/{gameId}/rounds")
public class RoundController {

    private RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping(produces = "application/json")
    @Authenticated
    public ResponseEntity<?> createNewRound(@PathVariable("gameId") int gameId) {
        return ok(roundService.createNewRound(gameId));
    }
}
