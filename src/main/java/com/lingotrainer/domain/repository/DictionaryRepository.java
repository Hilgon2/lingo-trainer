package com.lingotrainer.domain.repository;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DictionaryRepository {
    Dictionary save(Dictionary dictionary);
    Optional<Dictionary> findByLanguage(String language);
    boolean existsByWord(String languageCode, String guessedWord);
    String retrieveRandomWord(String languageCode, WordLength wordLength);
    List<String> findAvailableLanguages();
    boolean delete(String language);
    List<String> findWordsByLanguage(String language) throws IOException;
}
