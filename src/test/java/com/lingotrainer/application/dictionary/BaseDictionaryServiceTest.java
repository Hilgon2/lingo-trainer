package com.lingotrainer.application.dictionary;

import com.lingotrainer.application.exception.GeneralException;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.dictionary.WordFilter;
import com.lingotrainer.domain.repository.DictionaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BaseDictionaryServiceTest {

    @Mock
    private DictionaryRepository mockDictionaryRepository;
    @Mock
    private WordFilter mockLingoWordFilter;

    private BaseDictionaryService mockDictionaryService;

    private static List<String> words = new ArrayList<>(Collections.singletonList("woord, pizza, moeder"));

    private final static String language = "test-nl_nl";
    private final static String language2 = "test-en_en";

    @BeforeEach
    void setUp() {
        initMocks(this);
        mockDictionaryService = new BaseDictionaryService(mockDictionaryRepository, mockLingoWordFilter);
    }

    static Stream<Arguments> provideDictionarySave() {
        return Stream.of(
                Arguments.of(
                        new MockMultipartFile("wordsFile", language, "application/json", words.toString().getBytes()),
                        false,
                        Dictionary
                                .builder()
                                .language(language)
                                .build(),
                        Dictionary
                                .builder()
                                .language(language)
                                .words(words)
                                .build(),
                        Dictionary
                                .builder()
                                .language(language)
                                .words(words)
                                .build()
                ),
                Arguments.of(
                        new MockMultipartFile("wordsFile", language, "application/json", "".getBytes()),
                        true,
                        Dictionary
                                .builder()
                                .language(language)
                                .build(),
                        Dictionary
                                .builder()
                                .language(language)
                                .words(words)
                                .build(),
                        Dictionary
                                .builder()
                                .language(language)
                                .words(words)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDictionarySave")
    @DisplayName("Save a dictionary")
    void test_save_dictionary(MockMultipartFile file, boolean fileExists, Dictionary dictionary, Dictionary newDictionary, Dictionary expectedResult) {
        when(mockLingoWordFilter.verify("word", words)).thenReturn(true);
        when(mockDictionaryRepository.save(any())).thenReturn(newDictionary);

        // Run the test
        final Dictionary result = mockDictionaryService.save(file, dictionary.getLanguage());

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideDictionarySaveException() {
        return Stream.of(
                Arguments.of(
                        new MockMultipartFile("wordsFile", language, "application/json", words.toString().getBytes()),
                        Dictionary
                                .builder()
                                .language(language)
                                .build(),
                        language
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDictionarySaveException")
    @DisplayName("Save a dictionary with invalid file. Should throw exception")
    void test_save_dictionary_exception(MockMultipartFile file, Dictionary dictionary, String language) {
        when(mockLingoWordFilter.verify("word", words)).thenReturn(true);
        when(mockDictionaryRepository.save(any())).thenThrow(GeneralException.class);

        // Verify the results
        assertThrows(GeneralException.class, () -> mockDictionaryService.save(file, language));
    }

    static Stream<Arguments> provideWordExists() {
        return Stream.of(
                Arguments.of(language, "schoen", true, true),
                Arguments.of(language2, "groen", false, false),
                Arguments.of(language, "forest", false, false),
                Arguments.of(language, "toets", true, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWordExists")
    @DisplayName("Test if word exists or not")
    void test_word_exist(String language, String guessedWord, boolean exists, boolean shouldAccept) {
        when(mockDictionaryRepository.existsByWord(language, guessedWord)).thenReturn(exists);

        // Run the test
        final boolean result = mockDictionaryService.existsByWord(language, guessedWord);

        // Verify the results
        assertEquals(result, shouldAccept);
    }

    static Stream<Arguments> provideRandomWordRetriever() {
        return Stream.of(
                Arguments.of(language, WordLength.FIVE, "groen", "groen"),
                Arguments.of(language, WordLength.SIX, "schoen", "schoen"),
                Arguments.of(language, WordLength.SEVEN, "alsmaar", "alsmaar"),
                Arguments.of(language, null, "broek", "broek")
        );
    }

    @ParameterizedTest
    @MethodSource("provideRandomWordRetriever")
    @DisplayName("Retrieve random word")
    void test_retrieve_random_word(String language, WordLength wordLength, String wordResult, String expectedResult) {
        when(mockDictionaryRepository.retrieveRandomWord(language, wordLength)).thenReturn(wordResult);

        // Run the test
        final String result = mockDictionaryService.retrieveRandomWord(language, wordLength);

        // Verify the results
        assertEquals(expectedResult, result);

    }

    @Test
    @DisplayName("Retrieve available languages")
    void test_find_available_languages() {
        when(mockDictionaryRepository.findAvailableLanguages()).thenReturn(List.of(language, language2));

        // Run the test
        final List<String> result = mockDictionaryService.findAvailableLanguages();

        // Verify the results
        assertEquals(2, result.size());
        assertEquals(List.of(language, language2), result);
    }
}
