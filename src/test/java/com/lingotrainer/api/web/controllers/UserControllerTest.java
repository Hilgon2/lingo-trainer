package com.lingotrainer.api.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingotrainer.api.security.jwt.JwtProperties;
import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import com.lingotrainer.api.web.mapper.UserFormMapper;
import com.lingotrainer.api.web.request.CreateUserRequest;
import com.lingotrainer.api.web.response.UserResponse;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.user.UserService;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.List;

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
    private UserService mockUserService;
    @MockBean
    private AuthenticationService mockAuthenticationService;
    @MockBean
    private UserFormMapper mockUserFormMapper;

    @Test
    void testCreateUser() throws Exception {
        // Setup

        // Configure UserFormMapper.convertToDomainEntity(...).
        final User user = new User(new UserId(0), "username", "password", 0, Role.TRAINEE, false, List.of(new GameId(0)));
        when(mockUserFormMapper.convertToDomainEntity(new CreateUserRequest("username", "password", Role.TRAINEE, false))).thenReturn(user);

        when(mockUserFormMapper.convertToResponse(new User(new UserId(0), "username", "password", 0, Role.TRAINEE, false, List.of(new GameId(0))))).thenReturn(new UserResponse("username", 0));

        // Configure UserService.createNewUser(...).
        final User user1 = new User(new UserId(0), "username", "password", 0, Role.TRAINEE, false, List.of(new GameId(0)));
        when(mockUserService.createNewUser(new User(new UserId(0), "username", "password", 0, Role.TRAINEE, false, List.of(new GameId(0))))).thenReturn(user1);

        final CreateUserRequest createUserRequest = CreateUserRequest.builder().username("username").password("password").role(Role.TRAINEE).build();
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/users")
                .content(this.objectMapper.writer().writeValueAsString(createUserRequest)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void testFindLoggedInUserDetails() throws Exception {
        // Setup
        when(mockUserFormMapper.convertToResponse(new User(new UserId(0), "username", "password", 0, Role.TRAINEE, false, List.of(new GameId(0))))).thenReturn(new UserResponse("username", 0));

        // Configure AuthenticationService.getUser(...).
        final User user = new User(new UserId(0), "username", "password", 0, Role.TRAINEE, false, List.of(new GameId(0)));
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
