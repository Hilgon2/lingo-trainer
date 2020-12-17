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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserFormMapperTest {

    private UserFormMapper userFormMapperUnderTest;
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
        userFormMapperUnderTest = new UserFormMapper();
    }

    @Test
    @DisplayName("Convert to domain entity")
    void test_convert_to_domain_entity() {
        // Setup
        final User expectedResult = user;

        // Run the test
        final User result = userFormMapperUnderTest.convertToDomainEntity(userRequest);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Convert to response")
    void test_convert_to_response() {
        // Setup
        final UserResponse expectedResult = userResponse;

        // Run the test
        final UserResponse result = userFormMapperUnderTest.convertToResponse(user);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
