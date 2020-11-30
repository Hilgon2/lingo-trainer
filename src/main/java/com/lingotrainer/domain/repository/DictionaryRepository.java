package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.dictionary.Dictionary;

import java.util.Optional;

public interface DictionaryRepository {
    Dictionary save(Dictionary dictionary);
    Optional<Dictionary> findByLanguage(String languageCode);
    boolean existsByWord(String languageCode, String guessedWord);
}
