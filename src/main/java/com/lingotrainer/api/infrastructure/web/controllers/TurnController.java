package com.lingotrainer.api.infrastructure.web.controllers;

import com.lingotrainer.api.application.game.round.turn.TurnService;
import com.lingotrainer.api.util.annotation.Authenticated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games/{gameId}/rounds/{roundId}/turns")
public class TurnController {

    private TurnService turnService;

    public TurnController(TurnService turnService) {
        this.turnService = turnService;
    }

    @PutMapping
    @Authenticated
    public ResponseEntity<?> playTurn(@PathVariable int gameId, @PathVariable int roundId, @RequestParam String guessedWord) {
        return ok(this.turnService.finishTurn(roundId, guessedWord));
    }
}
