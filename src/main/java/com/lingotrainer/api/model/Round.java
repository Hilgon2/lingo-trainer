package com.lingotrainer.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rounds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String word;

    @OneToMany(mappedBy = "round")
    @Builder.Default
    private List<Turn> turns = new ArrayList<>();

    @Transient
    private int lettersCount;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private Game game;
}
