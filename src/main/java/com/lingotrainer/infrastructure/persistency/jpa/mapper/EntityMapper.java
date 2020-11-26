package com.lingotrainer.infrastructure.persistency.jpa.mapper;

import java.util.List;

public interface EntityMapper<D, P> {
    D convertToDomainEntity(P p);
    P convertToPersistableEntity (D d);
    List<P> convertToPersistableEntities(List<D> d);
    List<D> convertToDomainEntities(List<P> d);
}
