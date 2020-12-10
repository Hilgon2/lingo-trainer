package com.lingotrainer.domain;

import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DictionaryTest {

    Dictionary dictionary;
    String language;

    @BeforeEach
    void setup() {
        this.dictionary = Dictionary
                .builder()
                .language(this.language)
                .words(new ArrayList<>(Arrays.asList("vader", "broer", "kabel", "moeder", "wortel", "donker", "actrice", "hakbijl", "zesvoud")))
                .build();
    }

    static Stream<Arguments> provideWords() {
        return Stream.of(
                Arguments.of("nicht"),
                Arguments.of("piraat"),
                Arguments.of("kussen"),
                Arguments.of("wagen"),
                Arguments.of("pikant"),
                Arguments.of("alsmaar")
        );
    }

    static Stream<Arguments> provideWordLengths() {
        return Stream.of(
                Arguments.of(WordLength.FIVE, 5),
                Arguments.of(WordLength.FIVE, 5),
                Arguments.of(WordLength.SIX, 6),
                Arguments.of(WordLength.SIX, 6),
                Arguments.of(WordLength.SEVEN, 7),
                Arguments.of(WordLength.SEVEN, 7)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWords")
    @DisplayName("Add new word to dictionary")
    void addNewWordToDictionary(String word) {
        List<String> words = this.dictionary.getWords();
        words.add(word);
        this.dictionary.addWord(word);

        assertEquals(words, this.dictionary.getWords());
    }

    @ParameterizedTest
    @MethodSource("provideWordLengths")
    @DisplayName("Get random next word")
    void getNextRandomWord(WordLength wordLength, int expectedLength) {
        assertEquals(this.dictionary.getRandomWord(wordLength).length(), expectedLength);
    }
}
