package com.lingotrainer.infrastructure.persistency.file.dictionary;

import com.google.gson.Gson;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.application.exception.GeneralException;
import com.lingotrainer.application.exception.NotFoundException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class BaseDictionaryFileRepository implements DictionaryRepository {

    private Gson gson = new Gson();

    @Override
    public Dictionary save(Dictionary dictionary) {
        try (FileWriter targetFileWriter = new FileWriter(String.format("src/main/resources/dictionary/%s.json",
                dictionary.getLanguage()))) {
            targetFileWriter.write(this.gson.toJson(dictionary.getWords()));
        } catch (IOException e) {
            throw new GeneralException(String.format("Unknown error trying to open the %s language file",
                    dictionary.getLanguage()));
        }

        return dictionary;
    }

    @Override
    public Optional<Dictionary> findByLanguage(String languageCode) {
        try {
            String targetFileReader = new String(Files.readAllBytes(Paths.get(
                    String.format("src/main/resources/dictionary/%s.json", languageCode)
            )));
            List<String> words = gson.fromJson(targetFileReader, List.class);

            return Optional.ofNullable(Dictionary.builder()
                    .language(languageCode)
                    .words(words)
                    .build());
        } catch (IOException e) {
            throw new NotFoundException(
                    "Someting went wrong trying to read the language file. The file might not exist."
            );
        }
    }

    @Override
    public boolean existsByWord(String languageCode, String guessedWord) {
        return this.findByLanguage(languageCode).orElseThrow(() ->
                new NotFoundException(String.format("Dictionary by language %s could not be found", languageCode)))
                .getWords().contains(guessedWord);
    }

    @Override
    public String retrieveRandomWord(String languageCode, WordLength wordLength) {
        Dictionary dictionary = this.findByLanguage(languageCode).orElseThrow(() ->
                new NotFoundException(String.format("Dictionary language %s could not be found", languageCode)));
        return dictionary.getRandomWord(wordLength);
    }
}
