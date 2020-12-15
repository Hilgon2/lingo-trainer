package com.lingotrainer.domain;

import com.lingotrainer.domain.model.dictionary.LingoWordFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordFilterTest {
    private LingoWordFilter wordFilter;
    private List<String> words = new ArrayList<>(List.of("exists"));

    @BeforeEach
    void setup() {
        this.wordFilter = new LingoWordFilter();
    }

    static Stream<Arguments> provideWordsWithDifferentLengths() {
        return Stream.of(
                Arguments.of("bot", false),
                Arguments.of("vier", false),
                Arguments.of("groen", true),
                Arguments.of("schoen", true),
                Arguments.of("alsmaar", true),
                Arguments.of("exists", false),
                Arguments.of("artiest", true),
                Arguments.of("hond", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWordsWithDifferentLengths")
    @DisplayName("Accept words of 5 to 7 letters")
    void test_verify_words_of_5_6_7_letters(String word, boolean shouldAccept) {
        assertEquals(shouldAccept, this.wordFilter.verify(word, this.words));
    }

    static Stream<Arguments> provideWordsWithDifferentSymbols() {
        return Stream.of(
                Arguments.of("bot", false),
                Arguments.of("vier", false),
                Arguments.of("groen", true),
                Arguments.of("gr0en", false),
                Arguments.of("groEn", false),
                Arguments.of("schoen", true),
                Arguments.of("sch oen", false),
                Arguments.of("SchOeN", false),
                Arguments.of("schoen!", false),
                Arguments.of("alsmaar", true),
                Arguments.of("artiest", true),
                Arguments.of("art1es!", false),
                Arguments.of("ARTIEST", false),
                Arguments.of("ARTiesT", false),
                Arguments.of("hond", false),
                Arguments.of("exists", false),
                Arguments.of("!!!!!", false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWordsWithDifferentSymbols")
    @DisplayName("Accept words with lowercase")
    void test_accept_words_with_lowercase(String word, boolean shouldAccept) {
        assertEquals(shouldAccept, this.wordFilter.verify(word, this.words));
    }
}
