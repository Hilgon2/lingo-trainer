package com.lingotrainer.api.infrastructure.web.controllers;

import com.lingotrainer.api.application.game.round.RoundService;
import com.lingotrainer.api.infrastructure.web.mapper.RoundFormMapper;
import com.lingotrainer.api.infrastructure.web.response.NewRoundResponse;
import com.lingotrainer.api.util.annotation.Authenticated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games/{gameId}/rounds")
public class RoundController {

    private RoundService roundService;
    private RoundFormMapper roundFormMapper;

    public RoundController(RoundService roundService, RoundFormMapper roundFormMapper) {
        this.roundService = roundService;
        this.roundFormMapper = roundFormMapper;
    }

    @PostMapping(produces = "application/json")
    @Authenticated
    public ResponseEntity<NewRoundResponse> createNewRound(@PathVariable("gameId") int gameId) {
        NewRoundResponse newRoundResponse = this.roundFormMapper.convertToResponse(roundService.createNewRound(gameId));
        return ok(newRoundResponse);
    }
}
