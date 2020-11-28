package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.AddDictionaryWordResponse;
import com.lingotrainer.domain.model.dictionary.Dictionary;

public class DictionaryFormMapper {
    public AddDictionaryWordResponse convertToResponse(Dictionary dictionary) {
        return AddDictionaryWordResponse.builder()
                .language(dictionary.getLanguage())
                .build();
    }
}
