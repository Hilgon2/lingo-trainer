package com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.PersistableEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.GameEntity;
import com.lingotrainer.api.infrastructure.persistency.jpa.entity.game.round.turn.TurnEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rounds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RoundEntity extends PersistableEntity {
    @NotNull
    private String word;

    @OneToMany(mappedBy = "round")
    @Builder.Default
    private List<TurnEntity> turns = new ArrayList<>();

    private boolean active;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private GameEntity game;
}
