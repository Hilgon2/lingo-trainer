package com.lingotrainer.infrastructure.persistency.jpa.repository.base;

import com.lingotrainer.domain.model.user.User;
import com.lingotrainer.domain.model.user.UserId;
import com.lingotrainer.infrastructure.persistency.jpa.entity.user.UserEntity;
import com.lingotrainer.infrastructure.persistency.jpa.mapper.UserMapper;
import com.lingotrainer.infrastructure.persistency.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class BaseUserJpaRepositoryTest {

    @Mock
    private UserJpaRepository mockUserJpaRepository;
    @Mock
    private UserMapper mockUserMapper;

    private BaseUserJpaRepository mockBaseUserJpaRepository;

    private static User user;
    private static UserEntity userEntity;

    @BeforeAll
    static void init() {
        user = User
                .builder()
                .userId(new UserId(1))
                .username("testaccount")
                .password("testaccountpassword")
                .build();

        userEntity = UserEntity
                .builder()
                .id(1)
                .username("testaccount")
                .password("testaccountpassword")
                .build();
    }

    @BeforeEach
    void setUp() {
        initMocks(this);
        mockBaseUserJpaRepository = new BaseUserJpaRepository(mockUserJpaRepository, mockUserMapper);
    }

    static Stream<Arguments> provideFindByUsername() {
        return Stream.of(
                Arguments.of(
                        "testaccount",
                        userEntity,
                        user,
                        Optional.ofNullable(User
                                .builder()
                                .userId(new UserId(1))
                                .username("testaccount")
                                .password("testaccountpassword")
                                .build())
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByUsername")
    @DisplayName("Find by username")
    void test_find_by_username(String username, UserEntity userEntity, User user, Optional<User> expectedResult) {
        when(mockUserMapper.convertToDomainEntity(userEntity)).thenReturn(user);
        when(mockUserJpaRepository.findByUsername(username)).thenReturn(userEntity);

        // Run the test
        final Optional<User> result = mockBaseUserJpaRepository.findByUsername(username);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideFindByUsernameNotFound() {
        return Stream.of(
                Arguments.of("accounttest", Optional.empty())
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByUsernameNotFound")
    @DisplayName("User not found by username")
    void test_find_by_username_not_found(String username, Optional<User> expectedResult) {
        when(mockUserMapper.convertToDomainEntity(null)).thenReturn(null);
        when(mockUserJpaRepository.findByUsername("username")).thenReturn(null);

        // Run the test
        final Optional<User> result = mockBaseUserJpaRepository.findByUsername(username);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("User exists by username")
    void test_exists_by_username() {
        when(mockUserJpaRepository.existsByUsername("username")).thenReturn(true);

        // Run the test
        final boolean result = mockBaseUserJpaRepository.existsByUsername("username");

        // Verify the results
        assertTrue(result);
    }

    static Stream<Arguments> provideSaveUser() {
        return Stream.of(
                Arguments.of(
                        user,
                        userEntity,
                        User
                                .builder()
                                .userId(new UserId(1))
                                .username("testaccount")
                                .password("testaccountpassword")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideSaveUser")
    @DisplayName("Save a user")
    void test_save_user(User user, UserEntity userEntity, User expectedResult) {
        when(mockUserMapper.convertToDomainEntity(userEntity)).thenReturn(user);
        when(mockUserJpaRepository.save(userEntity)).thenReturn(userEntity);
        when(mockUserMapper.convertToPersistableEntity(user)).thenReturn(userEntity);

        // Run the test
        final User result = mockBaseUserJpaRepository.save(user);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideFindById() {
        return Stream.of(
                Arguments.of(
                        1,
                        user,
                        userEntity,
                        User
                                .builder()
                                .userId(new UserId(1))
                                .username("testaccount")
                                .password("testaccountpassword")
                                .build()
                )
        );
    }


    @ParameterizedTest
    @MethodSource("provideFindById")
    @DisplayName("Find by ID")
    void test_find_user_by_id(int userId, User user, UserEntity userEntity, User expectedResult) {
        when(mockUserMapper.convertToDomainEntity(userEntity)).thenReturn(user);
        when(mockUserJpaRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Run the test
        final Optional<User> result = mockBaseUserJpaRepository.findById(userId);

        // Verify the results
        assertEquals(Optional.of(expectedResult), result);
    }

    static Stream<Arguments> provideFindByIdNotFound() {
        return Stream.of(
                Arguments.of(
                        31,
                        user,
                        userEntity,
                        Optional.empty()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindByIdNotFound")
    @DisplayName("Find by ID user not found")
    void test_find_user_by_id_not_found(int userId, User user, UserEntity userEntity, Optional<User> expectedResult) {
        when(mockUserMapper.convertToDomainEntity(userEntity)).thenReturn(user);
        when(mockUserJpaRepository.findById(userId)).thenReturn(Optional.empty());

        // Run the test
        final Optional<User> result = mockBaseUserJpaRepository.findById(userId);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
