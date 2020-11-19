package com.lingotrainer.api.infrastructure.persistency.jpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@MappedSuperclass
public class PersistableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
}
