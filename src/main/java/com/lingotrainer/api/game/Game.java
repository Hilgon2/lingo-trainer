package com.lingotrainer.api.game;

import com.lingotrainer.api.game.round.Round;
import com.lingotrainer.api.user.User;
import lombok.*;

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
public class Game  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User user;

    private int score;

    @NotNull
    private String language;

    @OneToMany(mappedBy = "game")
    @Builder.Default
    private List<Round> rounds = new ArrayList<>();

    @Column(name="game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    public enum GameStatus {
        ACTIVE,
        FINISHED
    }
}
