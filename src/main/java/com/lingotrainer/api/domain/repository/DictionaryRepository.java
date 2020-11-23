package com.lingotrainer.api.domain.repository;

import com.lingotrainer.api.domain.model.dictionary.Dictionary;

public interface DictionaryRepository {
    void save(Dictionary dictionary);
}
