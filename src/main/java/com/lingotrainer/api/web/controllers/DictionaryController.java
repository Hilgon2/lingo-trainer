package com.lingotrainer.api.web.controllers;

import com.lingotrainer.application.dictionary.DictionaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/dictionary")
public class DictionaryController {

    private DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> save(@RequestParam("wordsFile") MultipartFile file,
                                       @RequestParam("languageCode") String languageCode) {
        return ok(dictionaryService.save(file, languageCode));
    }
}
