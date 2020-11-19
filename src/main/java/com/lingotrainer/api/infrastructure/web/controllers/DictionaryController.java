package com.lingotrainer.api.infrastructure.web.controllers;

import com.google.gson.Gson;
import com.lingotrainer.api.application.dictionary.DictionaryService;
import com.lingotrainer.api.domain.model.dictionary.Dictionary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/dictionary")
public class DictionaryController {

    private DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestParam("wordsFile") MultipartFile file, @RequestParam("languageCode") String languageCode) {
        Gson gson = new Gson();
        try {
            Dictionary dictionary;
            // if file exists, get words list. Otherwise create empty word list.
            if (new File(String.format("src/main/resources/dictionary/%s.json", languageCode)).exists()) {
                String targetFileReader = new String(Files.readAllBytes(Paths.get(String.format("src/main/resources/dictionary/%s.json", languageCode))));
                dictionary = gson.fromJson(targetFileReader, Dictionary.class);
            } else {
                dictionary = Dictionary.builder()
                        .words(new ArrayList<>())
                        .build();
            }

            dictionary.setLanguage(languageCode);

            dictionaryService.save(dictionary, file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
