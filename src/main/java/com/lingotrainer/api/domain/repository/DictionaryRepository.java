package com.lingotrainer.api.domain.repository;

import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import org.springframework.web.multipart.MultipartFile;

public interface DictionaryRepository {
    void save(Dictionary dictionary, MultipartFile file);
}
