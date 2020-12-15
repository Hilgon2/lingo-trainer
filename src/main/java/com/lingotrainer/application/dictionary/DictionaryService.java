package com.lingotrainer.application.dictionary;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DictionaryService {
    /**
     * Create or update a dictionary, depending on if the language already exists or not.
     *
     * @param file         the file which contains the new words
     * @param languageCode the language of the dictionary
     * @return the language code
     */
    Dictionary save(MultipartFile file, String languageCode);

    /**
     * Check if word exists in dictionary.
     * @param languageCode language of the word
     * @param guessedWord the word itself
     * @return true or false
     */
    boolean existsByWord(String languageCode, String guessedWord);

    /**
     * Get random word of dictionary.
     * @param languageCode language of the word / dictionary
     * @param wordLength length of the word
     * @return new word based on length
     */
    String retrieveRandomWord(String languageCode, WordLength wordLength);

    /**
     * Get a list of all the available languages
     * @return list of available languages
     */
    List<String> findAvailableLanguages();
}
