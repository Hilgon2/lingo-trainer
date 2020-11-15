package com.lingotrainer.api.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;

    @NotNull
    private String word;

    private int score;

    @NotNull
    private String language;

    @OneToMany(mappedBy = "game")
    @Builder.Default
    private List<Turn> turns = new ArrayList<>();

    @Column(name="game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    public enum GameStatus {
        ACTIVE,
        FINISHED
    }
}
