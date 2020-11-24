package com.lingotrainer.api.infrastructure.persistency.jpa.entity.game;

import com.lingotrainer.api.domain.model.game.GameStatus;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    private int score;

    private String language;

    @OneToMany(mappedBy = "game")
    @Builder.Default
    private List<RoundEntity> rounds = new ArrayList<>();

    @Column(name="game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
}
