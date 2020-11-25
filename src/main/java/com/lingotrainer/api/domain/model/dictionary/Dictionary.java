package com.lingotrainer.api.domain.model.dictionary;

import com.lingotrainer.api.domain.model.WordLength;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Dictionary {
    List<String> words = new ArrayList<>();
    String language;

    public String getRandomWord(WordLength wordLength) {
        String randomWord;
        randomWord = this.words.get(new Random().nextInt(this.words.size()));

        if (randomWord.length() != wordLength.getLength()) {
            randomWord = getRandomWord(wordLength);
        }

        return randomWord;
    }
}
