package com.lingotrainer.application.dictionary;

import com.google.gson.Gson;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.application.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseDictionaryService implements DictionaryService {

    private DictionaryRepository dictionaryRepository;

    public BaseDictionaryService(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    /**
     * Create or update a dictionary, depending on if the language already exists or not.
     * @param file the file which contains the new words
     * @param languageCode the language of the dictionary
     * @return the language code
     */
    @Override
    public String save(MultipartFile file, String languageCode) {
        Gson gson = new Gson();
        Dictionary dictionary = Dictionary.builder()
                .language(languageCode)
                .build();

        try {
            // if file exists, get words list. Otherwise create empty word list.
            if (new File(String.format("src/main/resources/dictionary/%s.json", dictionary.getLanguage())).exists()) {
                String targetFileReader = new String(Files.readAllBytes(Paths.get(String.format("src/main/resources/dictionary/%s.json", dictionary.getLanguage()))));
                dictionary.setWords(gson.fromJson(targetFileReader, List.class));
            } else {
                dictionary.setWords(new ArrayList<>());
            }

            // add new words to words list
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() >= 5 && line.length() <= 7 && line.chars().allMatch(Character::isLetter) && !dictionary.getWords().contains(line)) {
                        dictionary.getWords().add(line.toUpperCase());
                    }
                }
            }

            return this.dictionaryRepository.save(dictionary);
        } catch (IOException e) {
            throw new GeneralException(String.format("Unknown error trying to open the %s language file", dictionary.getLanguage()));
        }
    }
}
