package com.lingotrainer.infrastructure.persistency.file.dictionary;

import com.lingotrainer.domain.model.dictionary.Dictionary;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface DictionaryPersistanceRepository {
    void save(Dictionary dictionary, MultipartFile file);
    Optional<Dictionary> findByLanguage(String languageCode);
    boolean existsByWord(String languageCode, String word);
}
