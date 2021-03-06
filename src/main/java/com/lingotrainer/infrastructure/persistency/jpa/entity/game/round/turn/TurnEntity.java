package com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.turn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingotrainer.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "turns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TurnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "guessed_word")
    private String guessedWord;

    @Column(name = "started_at")
    private Instant startedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private RoundEntity round;
}
