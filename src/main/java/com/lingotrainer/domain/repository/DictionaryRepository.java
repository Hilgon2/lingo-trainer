package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.dictionary.Dictionary;

import java.util.Optional;

public interface DictionaryRepository {
    String save(Dictionary dictionary);
    Optional<Dictionary> findByLanguage(String languageCode);
}
