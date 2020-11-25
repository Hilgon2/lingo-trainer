package com.lingotrainer.api.application.dictionary.base;

import com.google.gson.Gson;
import com.lingotrainer.api.application.dictionary.DictionaryService;
import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import com.lingotrainer.api.domain.repository.DictionaryRepository;
import com.lingotrainer.api.util.exception.GeneralException;
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

            List<String> additionalWords = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() >= 5 && line.length() <= 7 && line.chars().allMatch(Character::isLetter) && !dictionary.getWords().contains(line)) {
                        additionalWords.add(line.toUpperCase());
                    }
                }
            }

            dictionary.getWords().addAll(additionalWords);
            return this.dictionaryRepository.save(dictionary);
        } catch (IOException e) {
            throw new GeneralException(String.format("Unknown error trying to open the %s language file", dictionary.getLanguage()));
        }
    }
}
