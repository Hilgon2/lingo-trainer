package com.lingotrainer.api.util.mappers;

import java.util.List;

public interface EntityMapper<Domain, Persistable> {
    Domain convertToDomainEntity(Persistable p);
    Persistable convertToPersistableEntity (Domain d);
    List<Persistable> convertToPersistableEntities(List<Domain> d);
    List<Domain> convertToDomainEntities(List<Persistable> d);
}
