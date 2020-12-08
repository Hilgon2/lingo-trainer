package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.annotation.Authenticated;
import com.lingotrainer.api.web.mapper.DictionaryFormMapper;
import com.lingotrainer.api.web.response.AddDictionaryWordResponse;
import com.lingotrainer.application.dictionary.DictionaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(path = "/dictionary")
public class DictionaryController {

    private DictionaryService dictionaryService;
    private DictionaryFormMapper dictionaryFormMapper;

    public DictionaryController(DictionaryService dictionaryService, DictionaryFormMapper dictionaryFormMapper) {
        this.dictionaryService = dictionaryService;
        this.dictionaryFormMapper = dictionaryFormMapper;
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AddDictionaryWordResponse> save(@RequestParam("wordsFile") MultipartFile file,
                                                          @RequestParam("languageCode") String languageCode) {
        return ok(this.dictionaryFormMapper.convertToResponse(dictionaryService.save(file, languageCode)));
    }

    @GetMapping(path = "/languages")
    @Authenticated
    public ResponseEntity<List<String>> findAvailableLanguages() {
        return ok(this.dictionaryService.findAvailableLanguages());
    }
}
