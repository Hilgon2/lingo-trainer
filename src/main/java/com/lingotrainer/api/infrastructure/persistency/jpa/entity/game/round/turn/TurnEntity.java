package com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.turn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.PersistableEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.RoundEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "turns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TurnEntity extends PersistableEntity {
    @Column(name="guessed_word")
    private String guessedWord;

    @Column(name="started_at")
    private Instant startedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private RoundEntity round;
}
