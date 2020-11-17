package com.lingotrainer.api.dictionary;

import org.springframework.web.multipart.MultipartFile;

public interface DictionaryService {
    void save(Dictionary dictionary, MultipartFile file);
}
