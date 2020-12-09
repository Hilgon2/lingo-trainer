package com.lingotrainer.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingotrainer.ApiApplicationTests;
import com.lingotrainer.api.web.request.CreateGameRequest;
import com.lingotrainer.api.web.request.CreateUserRequest;
import com.lingotrainer.application.user.BaseUserService;
import com.lingotrainer.domain.model.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = ApiApplicationTests.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BaseUserService userService;

//    @Test
//    void create_new_trainee() throws Exception {
//        given(this.userService.createNewUser(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
//
//        CreateUserRequest user = CreateUserRequest
//                .builder()
//                .username("trainee1")
//                .password("wachtwoord123")
//                .build();
//
//        this.mockMvc.perform(post("/api/v1/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.objectMapper.writeValueAsString(user))
//        )
//                .andDo(print())
//        .andExpect(jsonPath("$.username", is(user.getUsername())));
//    }
}
