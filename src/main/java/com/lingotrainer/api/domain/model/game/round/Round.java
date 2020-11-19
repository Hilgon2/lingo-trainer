package com.lingotrainer.api.domain.model.game.round;

import com.lingotrainer.api.domain.DomainEntity;
import com.lingotrainer.api.domain.model.game.Game;
import com.lingotrainer.api.domain.model.game.round.turn.Turn;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Round extends DomainEntity {
    @ToString.Exclude
    private String word;

    @ToString.Exclude
    private List<Turn> turns = new ArrayList<>();

    private int lettersCount;

    @ToString.Exclude
    private Game game;

    private boolean active;

    public int getLettersCount() {
        return this.turns.size();
    }
}
