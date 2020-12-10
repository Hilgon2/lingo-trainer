package com.lingotrainer.api.web.controllers;

import com.lingotrainer.api.security.jwt.JwtProperties;
import com.lingotrainer.api.security.jwt.JwtTokenProvider;
import com.lingotrainer.api.web.mapper.DictionaryFormMapper;
import com.lingotrainer.api.web.response.AddDictionaryWordResponse;
import com.lingotrainer.application.authentication.AuthenticationService;
import com.lingotrainer.application.dictionary.DictionaryService;
import com.lingotrainer.application.user.UserService;
import com.lingotrainer.domain.model.dictionary.Dictionary;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DictionaryController.class)
@ContextConfiguration(classes = {JwtTokenProvider.class, JwtProperties.class})
@AutoConfigureMockMvc(addFilters = false)
@Import(DictionaryController.class)
class DictionaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DictionaryService mockDictionaryService;
    @MockBean
    private DictionaryFormMapper mockDictionaryFormMapper;
    @MockBean
    private UserService mockUserService;
    @MockBean
    private AuthenticationService mockAuthenticationService;

    private static String testLanguage = "nl_nl";
    private static List<String> words = new ArrayList<>(Collections.singletonList("woord, pizza, moeder"));
    private static Dictionary dictionary = Dictionary.builder()
                                .language(testLanguage)
                                .words(words)
                                .build();

    static Stream<Arguments> provideNewDictionaryWords() {
        return Stream.of(
                Arguments.of(
                        AddDictionaryWordResponse.builder()
                                .language(testLanguage)
                                .build(),
                        dictionary,
                        new MockMultipartFile("wordsFile", testLanguage, "application/json", words.toString().getBytes())
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideNewDictionaryWords")
    @DisplayName("Upload new words to dictionary")
    @WithMockUser(roles = "ADMIN")
    void saveTest(AddDictionaryWordResponse addDictionaryWordResponse, Dictionary dictionary, MockMultipartFile mockMultipartFile) throws Exception {
        when(mockDictionaryFormMapper.convertToResponse(any())).thenReturn(addDictionaryWordResponse);
        when(mockDictionaryService.save(any(), any())).thenReturn(dictionary);

        final MockHttpServletResponse response = mockMvc.perform(multipart("/dictionary")
                .file(mockMultipartFile)
                .param("languageCode", testLanguage)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        JSONObject jsonObject = new JSONObject(response.getContentAsString());

        // Verify the results
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(testLanguage, jsonObject.get("language"));
    }

    @Test
    @DisplayName("Find available languages")
    void findAvailableLanguagesTest() throws Exception {
        when(mockDictionaryService.findAvailableLanguages()).thenReturn(List.of(dictionary.getLanguage()));
        List<String> languages = List.of(dictionary.getLanguage());

        final MockHttpServletResponse response = mockMvc.perform(get("/dictionary/languages")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(new JSONArray(languages), new JSONArray(response.getContentAsString()));
    }
}
