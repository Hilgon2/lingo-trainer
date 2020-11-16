package com.lingotrainer.api.controller;

import com.google.gson.Gson;
import com.lingotrainer.api.model.Word;
import com.lingotrainer.api.security.component.AuthenticationFacade;
import com.lingotrainer.api.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/words")
public class WordController {

    private UserService userService;
    private final AuthenticationFacade authenticationFacade;


    public WordController(UserService userService, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestParam("wordsFile") MultipartFile file, @RequestParam("languageCode") String languageCode) {
        Gson gson = new Gson();
        Map<String, List<String>> wordsObject = new HashMap<>();
        try {
            Word words;
            // if file exists, get words list. Otherwise create empty word list.
            if (new File(String.format("src/main/resources/words/%s.json", languageCode)).exists()) {
                String targetFileReader= new String(Files.readAllBytes(Paths.get(String.format("src/main/resources/words/%s.json", languageCode))));
                words = gson.fromJson(targetFileReader, Word.class);
            } else {
                words = Word.builder()
                        .words(new ArrayList<>())
                        .language(languageCode)
                        .build();
            }

            FileWriter targetFileWriter = new FileWriter(String.format("src/main/resources/words/%s.json", languageCode));
            List<String> additionalWords = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() >= 5 && line.length() <= 7 && line.chars().allMatch(Character::isLetter) && !words.getWords().contains(line)) {
                        additionalWords.add(line);
                    }
                }
            }
            words.getWords().addAll(additionalWords);
            wordsObject.put("words", words.getWords());
            targetFileWriter.write(gson.toJson(wordsObject));
            targetFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
