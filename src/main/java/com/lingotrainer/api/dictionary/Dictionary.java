package com.lingotrainer.api.dictionary;

import com.google.gson.Gson;
import com.lingotrainer.api.shared.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public String getRandomWord() {
        String randomWord;
        try {
            Gson gson = new Gson();
            String targetFileReader = new String(Files.readAllBytes(Paths.get(String.format("src/main/resources/dictionary/%s.json", this.language))));
            this.words = gson.fromJson(targetFileReader, Dictionary.class).getWords();

            randomWord = this.words.get(new Random().nextInt(this.words.size()));
        } catch (IOException e) {
            throw new NotFoundException("Someting went wrong trying to read the language file. The file might not exist.");
        }

        return randomWord;
    }
}
