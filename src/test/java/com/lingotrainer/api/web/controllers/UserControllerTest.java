package com.lingotrainer.api.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingotrainer.api.web.mapper.UserFormMapper;
import com.lingotrainer.api.web.request.CreateUserRequest;
import com.lingotrainer.api.web.response.UserResponse;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.application.user.UserService;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(UserController.class)
class UserControllerTest extends TestController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService mockUserService;
    @MockBean
    private AuthenticationService mockAuthenticationService;
    @MockBean
    private UserFormMapper mockUserFormMapper;

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

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
    void test_should_create_user(CreateUserRequest createUserRequest, UserResponse userResponse, User user) throws Exception {
        when(mockUserFormMapper.convertToDomainEntity(any())).thenReturn(user);
        when(mockUserFormMapper.convertToResponse(any())).thenReturn(userResponse);
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/users")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .content(this.objectMapper.writer().writeValueAsString(createUserRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideUserCreation")
    @DisplayName("Do not create user with existing username")
    void test_should_not_create_user_with_existing_username(CreateUserRequest createUserRequest, UserResponse userResponse, User user) throws Exception {
        when(mockUserFormMapper.convertToDomainEntity(any())).thenReturn(user);
        when(mockUserFormMapper.convertToResponse(any())).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                .content(this.objectMapper.writer().writeValueAsString(createUserRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(userResponse.getUsername())))
                .andExpect(jsonPath("$.highscore", is(userResponse.getHighscore())))
                .andReturn().getResponse();

        when(mockUserService.createNewUser(any(), any())).thenThrow(new DuplicateException("Username duplicate"));

        mockMvc.perform(post("/users")
                .content(this.objectMapper.writer().writeValueAsString(createUserRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @ParameterizedTest
    @MethodSource("provideLoggedInUserDetails")
    @DisplayName("Find logged in user details")
    void test_find_logged_in_user_details(UserResponse userResponse, User user) throws Exception {
        when(mockUserFormMapper.convertToResponse(user)).thenReturn(userResponse);
        when(mockAuthenticationService.getUser()).thenReturn(user);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/users/me")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        JSONObject jsonObject = new JSONObject(response.getContentAsString());

        // Verify the results
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(userResponse.getUsername(), jsonObject.get("username"));
        assertEquals(userResponse.getHighscore(), jsonObject.get("highscore"));
    }
}
