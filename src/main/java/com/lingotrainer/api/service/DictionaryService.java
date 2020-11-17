package com.lingotrainer.api.service;

import com.lingotrainer.api.model.Dictionary;
import org.springframework.web.multipart.MultipartFile;

public interface DictionaryService {
    void save(Dictionary dictionary, MultipartFile file);
}
