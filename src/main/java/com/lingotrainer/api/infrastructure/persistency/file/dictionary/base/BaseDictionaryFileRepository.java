package com.lingotrainer.api.infrastructure.persistency.file.dictionary.base;

import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import com.lingotrainer.api.domain.repository.DictionaryRepository;
import com.lingotrainer.api.infrastructure.persistency.file.dictionary.DictionaryFileRepository;
import org.springframework.web.multipart.MultipartFile;

public class BaseDictionaryFileRepository implements DictionaryRepository {
    DictionaryFileRepository dictionaryFileRepository;

    public BaseDictionaryFileRepository(DictionaryFileRepository dictionaryFileRepository) {
        this.dictionaryFileRepository = dictionaryFileRepository;
    }

    @Override
    public void save(Dictionary dictionary, MultipartFile file) {
        this.dictionaryFileRepository.save(dictionary, file);
    }
}
