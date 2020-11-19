package com.lingotrainer.api.infrastructure.persistency.jpa.entity.game;

import com.lingotrainer.api.infrastructure.persistency.jpa.entity.PersistableEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity extends PersistableEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private UserEntity user;

    private int score;

    @NotNull
    private String language;

    @OneToMany(mappedBy = "game")
    @Builder.Default
    private List<RoundEntity> rounds = new ArrayList<>();

    @Column(name="game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    public enum GameStatus {
        ACTIVE,
        FINISHED
    }
}
