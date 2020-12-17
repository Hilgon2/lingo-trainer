package com.lingotrainer.application.user;

import com.lingotrainer.application.exception.DuplicateException;
import com.lingotrainer.application.exception.ForbiddenException;
import com.lingotrainer.application.exception.NotFoundException;
import com.lingotrainer.domain.model.game.GameId;
import com.lingotrainer.domain.model.user.Role;
import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BaseUserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    private BaseUserService mockUserService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        mockUserService = new BaseUserService(mockUserRepository, mockPasswordEncoder);
    }

    static Stream<Arguments> provideUserSaveData() {
        return Stream.of(
                Arguments.of(
                    User.builder()
                        .userId(new UserId(0))
                            .username("username")
                            .password("wachtwoord123")
                            .role(Role.TRAINEE)
                            .active(true)
                            .gameIds(new ArrayList<>())
                        .build(),
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserSaveData")
    @DisplayName("Save a user")
    void test_save_trainee_user(User newUser, User expectedResult) {
        when(mockUserRepository.save(newUser)).thenReturn(newUser);

        // Run the test
        final User result = mockUserService.save(newUser);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideUserCreationData() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        User.builder()
                                .userId(new UserId(8))
                                .username("existinguser")
                                .password("password123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("encodedPassword")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("wachtwoord123")
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        User.builder()
                                .userId(new UserId(8))
                                .username("existinguser")
                                .password("password123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("encodedPassword")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserCreationData")
    @DisplayName("Create a new user with non-existing username")
    void test_create_new_trainee_with_new_username(User newUser, User currentUser, User expectedResult) {
        when(mockUserRepository.existsByUsername(newUser.getUsername())).thenReturn(false);
        when(mockPasswordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");
        when(mockUserRepository.save(newUser)).thenReturn(newUser);

        // Run the test
        final User result = mockUserService.createNewUser(newUser, currentUser);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideExistingUserCreationData() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .userId(new UserId(0))
                                .username("existinguser")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        User.builder()
                                .userId(new UserId(8))
                                .username("existinguser")
                                .password("password123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideExistingUserCreationData")
    @DisplayName("Create a new user with existing username")
    void test_create_new_trainee_with_existing_username_throw_exception(User newUser, User currentUser) {
        when(mockUserRepository.existsByUsername(newUser.getUsername())).thenReturn(true);

        // Verify the results
        assertThrows(DuplicateException.class, () -> mockUserService.createNewUser(newUser, currentUser));
    }

    static Stream<Arguments> provideUserAsTraineeCreationData() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .userId(new UserId(0))
                                .username("username")
                                .password("wachtwoord123")
                                .active(true)
                                .role(Role.ADMIN)
                                .gameIds(new ArrayList<>())
                                .build(),
                        User.builder()
                                .userId(new UserId(8))
                                .username("existinguser")
                                .password("password123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .userId(new UserId(0))
                                .username("newtrainee")
                                .password("wachtwoord123")
                                .active(true)
                                .role(Role.ADMIN)
                                .gameIds(new ArrayList<>())
                                .build(),
                        User.builder()
                                .userId(new UserId(8))
                                .username("nontrainee")
                                .password("password123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserAsTraineeCreationData")
    @DisplayName("Create admin as non-admin. Should throw exception")
    void test_create_new_admin_as_non_admin(User newUser, User currentUser) {
        when(mockUserRepository.existsByUsername(newUser.getUsername())).thenReturn(false);
        when(mockPasswordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");
        when(mockUserRepository.save(newUser)).thenReturn(newUser);

        // Verify the results
        assertThrows(ForbiddenException.class, () -> mockUserService.createNewUser(newUser, currentUser));
    }

    static Stream<Arguments> providerLoadByUsername() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .userId(new UserId(1))
                                .username("username")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        "username",
                        User.builder()
                                .userId(new UserId(1))
                                .username("username")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .userId(new UserId(3))
                                .username("ditbenik")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        "ditbenik",
                        User.builder()
                                .userId(new UserId(3))
                                .username("ditbenik")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                        ),
                Arguments.of(
                        User.builder()
                                .userId(new UserId(6))
                                .username("terry")
                                .password("test123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        "terry",
                        User.builder()
                                .userId(new UserId(6))
                                .username("terry")
                                .password("test123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                        )
        );
    }

    @ParameterizedTest
    @MethodSource("providerLoadByUsername")
    @DisplayName("Load user details by username")
    void test_load_user_details_by_username(User user, String username, User expectedResult) {
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        // Run the test
        final UserDetails result = mockUserService.loadUserByUsername(username);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> providerLoadByUsernameAbsent() {
        return Stream.of(
                Arguments.of("username"),
                Arguments.of("traineeTest")
        );
    }

    @ParameterizedTest
    @MethodSource("providerLoadByUsernameAbsent")
    @DisplayName("Username does not exist. Should throw exception")
    void test_find_user_details_by_username_should_not_be_found(String username) {
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Verify the results
        assertThrows(NotFoundException.class, () -> mockUserService.loadUserByUsername(username));
    }

    static Stream<Arguments> provideFindById() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .userId(new UserId(1))
                                .username("username")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        new UserId(1),
                        User.builder()
                                .userId(new UserId(1))
                                .username("username")
                                .password("wachtwoord123")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .userId(new UserId(5))
                                .username("testTrainee")
                                .password("mijnwachtwoord")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build(),
                        new UserId(5),
                        User.builder()
                                .userId(new UserId(5))
                                .username("testTrainee")
                                .password("mijnwachtwoord")
                                .role(Role.TRAINEE)
                                .active(true)
                                .gameIds(new ArrayList<>())
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindById")
    @DisplayName("Find user by ID")
    void test_find_by_id(User user, UserId userId, User expectedResult) {
        when(mockUserRepository.findById(userId.getId())).thenReturn(Optional.ofNullable(user));

        // Run the test
        final User result = mockUserService.findById(userId.getId());

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideFindByIdAbsent() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(5),
                Arguments.of(9)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByIdAbsent")
    @DisplayName("User ID should not be found")
    void test_find_by_id_user_not_found(int userId) {
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        // Verify the results
        assertThrows(NotFoundException.class, () -> mockUserService.findById(userId));
    }

    @ParameterizedTest
    @MethodSource("providerLoadByUsername")
    @DisplayName("Find user by its username")
    void test_find_user_by_username(User user, String username, User expectedResult) {
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

        // Run the test
        final User result = mockUserService.findByUsername(username);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("providerLoadByUsernameAbsent")
    @DisplayName("Username does not exist. Should throw exception")
    void test_find_user_by_username_should_not_be_found(String username) {
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Verify the results
        assertThrows(NotFoundException.class, () -> mockUserService.findByUsername(username));
    }
}
