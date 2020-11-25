package com.lingotrainer.api.domain.repository;

import com.lingotrainer.api.domain.model.dictionary.Dictionary;

import java.util.Optional;

public interface DictionaryRepository {
    String save(Dictionary dictionary);
    Optional<Dictionary> findByLanguage(String languageCode);
}
