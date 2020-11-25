package com.lingotrainer.api.application.dictionary;

import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import org.springframework.web.multipart.MultipartFile;

public interface DictionaryService {
    String save(MultipartFile file, String languageCode);
}
