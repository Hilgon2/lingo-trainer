package com.lingotrainer.domain.model.dictionary;

import com.lingotrainer.domain.exception.DictionaryFileIOException;
import com.lingotrainer.domain.model.WordLength;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Dictionary {
    private List<String> words = new ArrayList<>();
    private String language;

    public void addNewWords(MultipartFile file, WordFilter lingoWordFilter) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (lingoWordFilter.verify(line, this.getWords())) {
                    this.addWord(line);
                }
            }
        } catch (IOException e) {
            throw new DictionaryFileIOException(String.format("Onbekende fout bij het openen van de woordenlijst %s",
                    this.getLanguage()));
        }
    }

    public void addWord(String word) {
        this.words.add(word.toUpperCase());
    }

    public String getRandomWord(WordLength wordLength) {
        String randomWord;
        randomWord = this.words.get(new Random().nextInt(this.words.size()));

        if (randomWord.length() != wordLength.getLength()) {
            randomWord = getRandomWord(wordLength);
        }

        return randomWord;
    }
}
