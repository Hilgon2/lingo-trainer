package com.lingotrainer.application.dictionary;

import com.lingotrainer.domain.model.dictionary.Dictionary;
import org.springframework.web.multipart.MultipartFile;

public interface DictionaryService {
    Dictionary save(MultipartFile file, String languageCode);
}
