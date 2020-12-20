package com.lingotrainer.api.web.mapper;

import com.lingotrainer.api.web.request.CreateUserRequest;
import com.lingotrainer.api.web.response.UserResponse;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserFormMapperTest {

    private UserFormMapper userFormMapperTest;
    private static User user;
    private static CreateUserRequest userRequest;
    private static UserResponse userResponse;

    @BeforeAll
    static void init() {
        user = User
                .builder()
                .userId(new UserId(1))
                .role(Role.TRAINEE)
                .username("myaccount")
                .password("mypassword")
                .highscore(0)
                .active(true)
                .gameIds(new ArrayList<>())
                .build();

        userRequest = CreateUserRequest
                .builder()
                .username("myaccount")
                .password("mypassword")
                .active(true)
                .role(Role.TRAINEE)
                .build();

        userResponse = UserResponse
                .builder()
                .highscore(0)
                .username("myaccount")
                .build();
    }

    @BeforeEach
    void setUp() {
        userFormMapperTest = new UserFormMapper();
    }

    @Test
    @DisplayName("Convert to domain entity")
    void test_convert_to_domain_entity() {
        // Setup
        final User expectedResult = user;

        // Run the test
        final User result = userFormMapperTest.convertToDomainEntity(userRequest);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Convert to response")
    void test_convert_to_response() {
        // Setup
        final UserResponse expectedResult = userResponse;

        // Run the test
        final UserResponse result = userFormMapperTest.convertToResponse(user);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideConvertToReponsesList() {
        return Stream.of(
                Arguments.of(new ArrayList<>(List.of(
                        User
                                .builder()
                                .username("tester1")
                                .highscore(16)
                                .role(Role.TRAINEE)
                                .build(),
                        User
                                .builder()
                                .username("tester2")
                                .highscore(11)
                                .role(Role.TRAINEE)
                                .build(),
                        User
                                .builder()
                                .username("tester3")
                                .highscore(7)
                                .role(Role.ADMIN)
                                .build(),
                        User
                                .builder()
                                .username("tester4")
                                .highscore(1)
                                .role(Role.TRAINEE)
                                .build()
                        )),
                        new ArrayList<>(List.of(
                        UserResponse
                                .builder()
                                .username("tester1")
                                .highscore(16)
                                .admin(false)
                                .build(),
                        UserResponse
                                .builder()
                                .username("tester2")
                                .highscore(11)
                                .admin(false)
                                .build(),
                        UserResponse
                                .builder()
                                .username("tester3")
                                .highscore(7)
                                .admin(true)
                                .build(),
                        UserResponse
                                .builder()
                                .username("tester4")
                                .highscore(1)
                                .admin(false)
                                .build()
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertToReponsesList")
    @DisplayName("Convert to responses list")
    void test_convert_to_responses_list(List<User> users, List<UserResponse> expectedResult) {
        final List<UserResponse> result = userFormMapperTest.convertToResponsesList(users);

        assertEquals(expectedResult, result);
    }
}
