package com.lingotrainer.application.dictionary;

import org.springframework.web.multipart.MultipartFile;

public interface DictionaryService {
    String save(MultipartFile file, String languageCode);
}