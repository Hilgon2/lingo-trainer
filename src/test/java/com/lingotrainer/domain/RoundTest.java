package com.lingotrainer.domain;

import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoundTest {
    static Stream<Arguments> provideRoundsForNextWordLength() {
        return Stream.of(
                Arguments.of(null, WordLength.FIVE),
                Arguments.of(Round.builder().wordLength(WordLength.FIVE).build(), WordLength.SIX),
                Arguments.of(Round.builder().wordLength(WordLength.SIX).build(), WordLength.SEVEN),
                Arguments.of(Round.builder().wordLength(WordLength.SEVEN).build(), WordLength.FIVE)
        );
    }

    static Stream<Arguments> provideRoundsForCurrentWordLength() {
        return Stream.of(
                Arguments.of(Round.builder().wordLength(WordLength.FIVE).build(), WordLength.FIVE),
                Arguments.of(Round.builder().wordLength(WordLength.SIX).build(), WordLength.SIX),
                Arguments.of(Round.builder().build(), WordLength.FIVE),
                Arguments.of(Round.builder().word("paard").build(), WordLength.FIVE),
                Arguments.of(Round.builder().word("reden").build(), WordLength.FIVE),
                Arguments.of(Round.builder().word("schoen").build(), WordLength.SIX),
                Arguments.of(Round.builder().word("moeder").build(), WordLength.SIX),
                Arguments.of(Round.builder().word("artiest").build(), WordLength.SEVEN),
                Arguments.of(Round.builder().word("alsmaar").build(), WordLength.SEVEN)
        );
    }

    static Stream<Arguments> provide_round_with_active_turns() {
        Round round = Round.builder().build();
        List<Turn> turns = new ArrayList<>(Arrays.asList(
                Turn.builder().build(),
                Turn.builder().build()
        ));
        return Stream.of(
                Arguments.of(round, turns)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoundsForNextWordLength")
    void get_next_word_length(Round lastRound, WordLength shouldAccept) {
        Round newRound = Round
                .builder()
                .build();
        newRound.nextWordLength(lastRound);
        assertEquals(shouldAccept, newRound.getWordLength());
    }

    @ParameterizedTest
    @MethodSource("provideRoundsForCurrentWordLength")
    void get_current_word_length(Round round, WordLength shouldAccept) {
        assertEquals(shouldAccept, round.getWordLength());
    }

    @ParameterizedTest
    @MethodSource("provide_round_with_active_turns")
    void active_turns_left(Round round, List<Turn> turns) {
        assertThrows(GameException.class, () -> round.checkActiveTurns(turns));
    }
}
