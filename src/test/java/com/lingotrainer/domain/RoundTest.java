package com.lingotrainer.domain;

import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

class RoundTest {
    static Stream<Arguments> provideRoundsForNextWordLength() {
        return Stream.of(
                Arguments.of(null, WordLength.FIVE),
                Arguments.of(Round.builder().roundId(new RoundId(4)).gameId(new GameId(2)).wordLength(WordLength.FIVE).build(), WordLength.SIX),
                Arguments.of(Round.builder().roundId(new RoundId(5)).gameId(new GameId(2)).wordLength(WordLength.SIX).build(), WordLength.SEVEN),
                Arguments.of(Round.builder().wordLength(WordLength.SEVEN).build(), WordLength.FIVE),
                Arguments.of(Round.builder().wordLength(WordLength.FIVE).build(), WordLength.SIX),
                Arguments.of(Round.builder().gameId(new GameId(5)).wordLength(WordLength.SIX).build(), WordLength.SEVEN)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundsForNextWordLength")
    @DisplayName("Get next word length")
    void test_get_next_word_length_depending_on_last_round(Round lastRound, WordLength shouldAccept) {
        Round newRound = Round
                .builder()
                .build();
        assertEquals(shouldAccept, newRound.retrieveNextWordLength(lastRound));
    }

    static Stream<Arguments> provideRoundsForCurrentWordLength() {
        return Stream.of(
                Arguments.of(Round.builder().wordLength(WordLength.FIVE).build(), WordLength.FIVE),
                Arguments.of(Round.builder().wordLength(WordLength.SIX).build(), WordLength.SIX),
                Arguments.of(Round.builder().build(), WordLength.FIVE),
                Arguments.of(Round.builder().word("paard").build(), WordLength.FIVE),
                Arguments.of(Round.builder().word("reden").build(), WordLength.FIVE),
                Arguments.of(Round.builder().word("schoen").build(), WordLength.SIX),
                Arguments.of(Round.builder().word("vier").build(), null),
                Arguments.of(Round.builder().word("drie").build(), null),
                Arguments.of(Round.builder().word("cd").build(), null),
                Arguments.of(Round.builder().word("moeder").build(), WordLength.SIX),
                Arguments.of(Round.builder().word("artiest").build(), WordLength.SEVEN),
                Arguments.of(Round.builder().word("alsmaar").build(), WordLength.SEVEN)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundsForCurrentWordLength")
    @DisplayName("Get current word length")
    void test_get_current_word_length(Round round, WordLength shouldAccept) {
        assertEquals(shouldAccept, round.getWordLength());
    }

    static Stream<Arguments> provideRoundWithActiveTurns() {
        return Stream.of(
                Arguments.of(Round.builder().roundId(new RoundId(2)).build(), new ArrayList<>(Collections.singletonList(
                        Turn.builder().build()
                ))),
                Arguments.of(Round.builder().roundId(new RoundId(3)).build(), new ArrayList<>(List.of(
                        Turn.builder().build(),
                        Turn.builder().build()
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundWithActiveTurns")
    @DisplayName("Throw error on active turns")
    void test_round_has_active_turns_exception(Round round, List<Turn> turns) {
        assertThrows(GameException.class, () -> round.checkActiveTurns(turns));
    }

    static Stream<Arguments> provideRoundWithNoActiveTurns() {
        return Stream.of(
                Arguments.of(Round.builder().roundId(new RoundId(2)).build(), new ArrayList<>()),
                Arguments.of(Round.builder().roundId(new RoundId(3)).build(), new ArrayList<>())
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundWithNoActiveTurns")
    @DisplayName("Do not throw an error with no active turns")
    void test_round_has_no_active_turns(Round round, List<Turn> turns) {
        assertDoesNotThrow(() -> round.checkActiveTurns(turns));
    }
}
