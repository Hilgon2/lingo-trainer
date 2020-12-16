package com.lingotrainer.application.dictionary;

import com.google.gson.Gson;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.dictionary.WordFilter;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.application.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseDictionaryService implements DictionaryService {

    private DictionaryRepository dictionaryRepository;
    private WordFilter lingoWordFilter;

    public BaseDictionaryService(DictionaryRepository dictionaryRepository, WordFilter lingoWordFilter) {
        this.dictionaryRepository = dictionaryRepository;
        this.lingoWordFilter = lingoWordFilter;
    }

    @Override
    public Dictionary save(MultipartFile file, String languageCode) {
        Gson gson = new Gson();
        Dictionary dictionary = Dictionary.builder()
                .language(languageCode)
                .build();
        String dictionaryPath = "src/main/resources/dictionary/%s.json";

        try {
            // if file exists, get words list. Otherwise create empty word list.
            if (this.dictionaryRepository.findByLanguage(dictionary.getLanguage()).isPresent()) {
                String targetFileReader = new String(Files.readAllBytes(Paths.get(
                        String.format(dictionaryPath, dictionary.getLanguage()))));
                dictionary.setWords(gson.fromJson(targetFileReader, List.class));
            } else {
                dictionary.setWords(new ArrayList<>());
            }

            // add new words to words list
            dictionary.addNewWords(file, lingoWordFilter);

            return this.dictionaryRepository.save(dictionary);
        } catch (IOException e) {
            throw new GeneralException(String.format("Onbekende fout bij het openen van de woordenlijst van %s",
                    dictionary.getLanguage()));
        }
    }

    @Override
    public boolean existsByWord(String languageCode, String guessedWord) {
        return this.dictionaryRepository.existsByWord(languageCode, guessedWord);
    }

    @Override
    public String retrieveRandomWord(String languageCode, WordLength wordLength) {
        return this.dictionaryRepository.retrieveRandomWord(languageCode, wordLength);
    }

    @Override
    public List<String> findAvailableLanguages() {
        return this.dictionaryRepository.findAvailableLanguages();
    }
}
