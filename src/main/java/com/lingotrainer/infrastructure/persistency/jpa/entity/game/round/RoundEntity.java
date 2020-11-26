package com.lingotrainer.infrastructure.persistency.jpa.entity.game.round;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rounds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String word;

    @OneToMany(mappedBy = "round")
    @Builder.Default
    private List<TurnEntity> turns = new ArrayList<>();

    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private GameEntity game;
}
