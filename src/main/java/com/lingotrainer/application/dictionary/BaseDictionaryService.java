package com.lingotrainer.application.dictionary;

import com.google.gson.Gson;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.dictionary.WordFilter;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.application.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    /**
     * Create or update a dictionary, depending on if the language already exists or not.
     *
     * @param file         the file which contains the new words
     * @param languageCode the language of the dictionary
     * @return the language code
     */
    @Override
    public Dictionary save(MultipartFile file, String languageCode) {
        Gson gson = new Gson();
        Dictionary dictionary = Dictionary.builder()
                .language(languageCode)
                .build();
        String dictionaryPath = "src/main/resources/dictionary/%s.json";

        try {
            // if file exists, get words list. Otherwise create empty word list.
            if (new File(String.format(dictionaryPath, dictionary.getLanguage())).exists()) {
                String targetFileReader = new String(Files.readAllBytes(Paths.get(
                        String.format(dictionaryPath, dictionary.getLanguage()))));
                dictionary.setWords(gson.fromJson(targetFileReader, List.class));
            } else {
                dictionary.setWords(new ArrayList<>());
            }

            // add new words to words list
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (this.lingoWordFilter.verify(line, dictionary.getWords())) {
                        dictionary.addWord(line);
                    }
                }
            }

            return this.dictionaryRepository.save(dictionary);
        } catch (IOException e) {
            throw new GeneralException(String.format("Unknown error trying to open the %s language file",
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
}
