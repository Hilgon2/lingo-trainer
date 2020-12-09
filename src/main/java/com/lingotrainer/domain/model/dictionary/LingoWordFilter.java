package com.lingotrainer.domain.model.dictionary;

import java.util.List;

public class LingoWordFilter implements WordFilter {
    @Override
    public boolean verify(String word, List<String> existingWords) {
        if (!existingWords.contains(word)) {
            return word.matches("^([a-z]{5,7})$");
        } else {
            return false;
        }
    }
}
