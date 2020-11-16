package com.lingotrainer.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "turns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="guessed_word")
    private String guessedWord;

    @Transient
    private List<GuessedLetter> guessedLetters;

    @Transient
    private boolean correctGuess;

    @Column(name="started_at")
    private Instant startedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private Round round;
}
