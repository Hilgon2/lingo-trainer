package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.response.RoundResponse;
import com.lingotrainer.domain.model.WordLength;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.TurnId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundFormMapperTest {

    private RoundFormMapper roundFormMapperUnderTest;

    @BeforeEach
    void setUp() {
        roundFormMapperUnderTest = new RoundFormMapper();
    }

    static Stream<Arguments> provideConvertToResponse() {
        return Stream.of(
                Arguments.of(
                        Round
                                .builder()
                                .roundId(new RoundId(1))
                                .gameId(new GameId(1))
                                .turnIds(new ArrayList<>())
                                .word("schoen")
                                .wordLength(WordLength.SIX)
                                .active(true)
                                .lettersCount(6)
                                .build(),
                        RoundResponse
                                .builder()
                                .firstLetter('s')
                                .lettersAmount(6)
                                .active(true)
                                .build()
                ),
                Arguments.of(
                        null,
                        RoundResponse
                                .builder()
                                .active(false)
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToResponse")
    @DisplayName("Convert to response")
    void test_convert_to_response(Round round, RoundResponse expectedResult) {
        // Run the test
        final RoundResponse result = roundFormMapperUnderTest.convertToResponse(round);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
