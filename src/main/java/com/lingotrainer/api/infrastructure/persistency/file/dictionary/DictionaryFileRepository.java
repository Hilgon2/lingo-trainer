package com.lingotrainer.api.infrastructure.persistency.file.dictionary;

import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import org.springframework.web.multipart.MultipartFile;

public interface DictionaryFileRepository {
    void save(Dictionary dictionary, MultipartFile file);
}
