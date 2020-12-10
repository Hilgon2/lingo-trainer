package com.lingotrainer.api.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingotrainer.api.security.jwt.JwtProperties;
import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import com.lingotrainer.api.web.mapper.UserFormMapper;
import com.lingotrainer.api.web.request.CreateUserRequest;
import com.lingotrainer.api.web.response.UserResponse;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {JwtTokenProvider.class, JwtProperties.class})
@AutoConfigureMockMvc(addFilters = false)
@Import(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService mockAuthenticationService;
    @MockBean
    private UserFormMapper mockUserFormMapper;

    static Stream<Arguments> provideUserCreation() {
        return Stream.of(
                Arguments.of(
                        CreateUserRequest.builder()
                                .username("username")
                                .password("password")
                                .role(Role.TRAINEE)
                                .active(true)
                                .build(),
                        UserResponse.builder()
                                .username("username")
                                .highscore(0)
                                .build(),
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("password")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                )
        );
    }

    static Stream<Arguments> provideLoggedInUserDetails() {
        return Stream.of(
                Arguments.of(
                        UserResponse.builder()
                                .username("username")
                                .highscore(0)
                                .build(),
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("password")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserCreation")
    @DisplayName("Create user")
    void createUserTest(CreateUserRequest createUserRequest, UserResponse userResponse, User user) throws Exception {
        when(mockUserFormMapper.convertToDomainEntity(createUserRequest)).thenReturn(user);
        when(mockUserFormMapper.convertToResponse(user)).thenReturn(userResponse);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/users")
                .content(this.objectMapper.writer().writeValueAsString(createUserRequest)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideLoggedInUserDetails")
    @DisplayName("Find logged in user details")
    void findLoggedInUserDetailsTest(UserResponse userResponse, User user) throws Exception {
        when(mockUserFormMapper.convertToResponse(user)).thenReturn(userResponse);
        when(mockAuthenticationService.getUser()).thenReturn(user);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/users/me")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        JSONObject jsonObject = new JSONObject(response.getContentAsString());

        // Verify the results
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("username", jsonObject.get("username"));
    }
}
