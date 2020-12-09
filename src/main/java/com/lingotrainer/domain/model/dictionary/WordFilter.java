package com.lingotrainer.domain.model.dictionary;

import java.util.List;

public interface WordFilter {
    boolean verify(String word, List<String> existingWords);
}
