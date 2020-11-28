package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.web.mapper.TurnFormMapper;
import com.lingotrainer.api.web.request.PlayTurnRequest;
import com.lingotrainer.api.web.response.PlayTurnResponse;
import com.lingotrainer.application.game.round.turn.TurnService;
import com.lingotrainer.api.annotation.Authenticated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/games/{gameId}/rounds/{roundId}/turns")
public class TurnController {

    private TurnService turnService;
    private TurnFormMapper turnFormMapper;

    public TurnController(TurnService turnService, TurnFormMapper turnFormMapper) {
        this.turnService = turnService;
        this.turnFormMapper = turnFormMapper;
    }

    @PutMapping
    @Authenticated
    public ResponseEntity<PlayTurnResponse> playTurn(@PathVariable int gameId,
                                                     @PathVariable int roundId,
                                                     @RequestBody PlayTurnRequest playTurnRequest) {
        return ok(this.turnFormMapper.convertToResponse(
                this.turnService.playTurn(roundId, playTurnRequest.getGuessedWord()))
        );
    }
}
