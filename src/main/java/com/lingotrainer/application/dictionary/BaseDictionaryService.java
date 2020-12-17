package com.lingotrainer.application.dictionary;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.dictionary.WordFilter;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.application.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Dictionary save(MultipartFile file, String language) {
        Dictionary dictionary = Dictionary.builder()
                .language(language)
                .build();

        try {
            // set words, because file is empty before filling
            dictionary.setWords(this.dictionaryRepository.findWordsByLanguage(dictionary.getLanguage()));

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
