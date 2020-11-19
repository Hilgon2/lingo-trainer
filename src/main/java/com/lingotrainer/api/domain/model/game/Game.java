package com.lingotrainer.api.domain.model.game;

import com.lingotrainer.api.domain.DomainEntity;
import com.lingotrainer.api.domain.model.game.round.Round;
import com.lingotrainer.api.domain.model.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game extends DomainEntity {
    private User user;

    private int score;

    private String language;

    @ToString.Exclude
    private List<Round> rounds = new ArrayList<>();

    private GameStatus gameStatus;

    public enum GameStatus {
        ACTIVE,
        FINISHED
    }
}
