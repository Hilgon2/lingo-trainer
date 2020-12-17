package com.lingotrainer.infrastructure.persistency.file.dictionary;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.infrastructure.persistency.exception.FileIOException;
import com.lingotrainer.infrastructure.persistency.exception.LanguageNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BaseDictionaryFileRepositoryTest {

    private static BaseDictionaryFileRepository mockBaseDictionaryFileRepository;
    private final static String language = "test-nl_nl";
    private final static String nonExistentLanguage = "test_test_test_test";
    private static Dictionary dictionary;

    @BeforeAll
    static void init() {
        dictionary = Dictionary
                .builder()
                .words(List.of("lopen", "schaap", "koeien", "varken", "alsmaar", "schippen"))
                .language(language)
                .build();
    }

    @BeforeEach
    void setUp() {
        mockBaseDictionaryFileRepository = new BaseDictionaryFileRepository();
    }

    @AfterAll
    static void after() {
        // delete dictionary after test
        mockBaseDictionaryFileRepository.delete(language);
    }

    static Stream<Arguments> provideSaveDictionary() {
        return Stream.of(
                Arguments.of(
                        dictionary,
                        Dictionary
                                .builder()
                                .words(List.of("lopen", "schaap", "koeien", "varken", "alsmaar", "schippen"))
                                .language(language)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveDictionary")
    @DisplayName("Save a dictionary")
    void test_save_dictionary(Dictionary dictionary, Dictionary expectedResult) {

        // Run the test
        final Dictionary result = mockBaseDictionaryFileRepository.save(dictionary);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideFindByLanguage() {
        return Stream.of(
                Arguments.of(language, Optional.ofNullable(dictionary)),
                Arguments.of(nonExistentLanguage, Optional.empty())
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByLanguage")
    @DisplayName("Find dictionary by language")
    void test_find_by_language(String language, Optional<Dictionary> expectedResult) {
        // Run the test
        final Optional<Dictionary> result = mockBaseDictionaryFileRepository.findByLanguage(language);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideFindByNonExistentLanguage() {
        return Stream.of(
                Arguments.of("test-dictionary")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByNonExistentLanguage")
    @DisplayName("Find dictionary by language which does not exist")
    void test_find_by_language_which_does_not_exists(String language) {
        final Optional<Dictionary> result = mockBaseDictionaryFileRepository.findByLanguage(language);

        assertEquals(Optional.empty(), result);
    }

    static Stream<Arguments> provideExistsByWord() {
        return Stream.of(
                Arguments.of(language, "spinnenwebben", false),
                Arguments.of(language, "koeien", true),
                Arguments.of(language, "varken", true),
                Arguments.of(language, "laptopscherm", false),
                Arguments.of(language, "lopen", true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideExistsByWord")
    @DisplayName("Word exists in dictionary")
    void test_exists_by_word(String language, String guessedWord, boolean expectedResult) {
        final boolean result = mockBaseDictionaryFileRepository.existsByWord(language, guessedWord);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideExistsByWordWithNonExistentDictionary() {
        return Stream.of(
                Arguments.of(nonExistentLanguage, "spinnenwebben"),
                Arguments.of(nonExistentLanguage, "koeien"),
                Arguments.of(nonExistentLanguage, "varken"),
                Arguments.of(nonExistentLanguage, "laptopscherm"),
                Arguments.of(nonExistentLanguage, "lopen")
        );
    }

    @ParameterizedTest
    @MethodSource("provideExistsByWordWithNonExistentDictionary")
    @DisplayName("Word exists in dictionary which does not exist")
    void test_exists_by_word_with_non_existent_dictionary(String language, String guessedWord) {
        // Verify the results
        assertThrows(LanguageNotFoundException.class, () -> mockBaseDictionaryFileRepository.existsByWord(language, guessedWord));
    }

    static Stream<Arguments> provideRandomWordRetriever() {
        return Stream.of(
                Arguments.of(language, WordLength.FIVE, 5),
                Arguments.of(language, WordLength.SIX, 6),
                Arguments.of(language, WordLength.SEVEN, 7)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRandomWordRetriever")
    @DisplayName("Get random word from dictionary")
    void test_retrieve_random_word(String language, WordLength wordLength, int expectedResult) {
        // Run the test
        final String result = mockBaseDictionaryFileRepository.retrieveRandomWord(language, wordLength);

        // Verify the results
        assertEquals(expectedResult, result.length());
    }

    static Stream<Arguments> provideRandomWordRetrieverUnknownLanguage() {
        return Stream.of(
                Arguments.of(nonExistentLanguage, WordLength.FIVE),
                Arguments.of(nonExistentLanguage, WordLength.SIX),
                Arguments.of(nonExistentLanguage, WordLength.SEVEN)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRandomWordRetrieverUnknownLanguage")
    @DisplayName("Get random word from dictionary which does not exist")
    void test_retrieve_random_word_non_existent_language(String language, WordLength wordLength) {
        // Verify the results
        assertThrows(LanguageNotFoundException.class, () -> mockBaseDictionaryFileRepository.retrieveRandomWord(language, wordLength));
    }

    static Stream<Arguments> provideAvailableLanguages() {
        return Stream.of(
                Arguments.of(language)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAvailableLanguages")
    @DisplayName("Find all available languages includes the test dictionary")
    void test_find_available_languages(String expectedResult) {
        // Run the test
        final List<String> result = mockBaseDictionaryFileRepository.findAvailableLanguages();

        // Verify the results
        assertTrue(result.contains(expectedResult));
    }

    static Stream<Arguments> provideFindWordsByLanguage() {
        return Stream.of(
                Arguments.of(language, new ArrayList<>(List.of("lopen", "schaap", "koeien", "varken", "alsmaar", "schippen"))),
                Arguments.of(nonExistentLanguage, new ArrayList<>())
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindWordsByLanguage")
    @DisplayName("Find the words by language. Return empty list of dictionary does not exist")
    void test_find_words_by_language(String language, List<String> expectedResult) throws IOException {
        // Run the test
        final List<String> result = mockBaseDictionaryFileRepository.findWordsByLanguage(language);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
