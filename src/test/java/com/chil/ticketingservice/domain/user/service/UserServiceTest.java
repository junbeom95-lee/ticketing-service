package com.chil.ticketingservice.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.common.utils.JwtUtil;
import com.chil.ticketingservice.domain.user.dto.request.UserCreateRequest;
import com.chil.ticketingservice.domain.user.dto.request.UserLoginRequest;
import com.chil.ticketingservice.domain.user.dto.response.UserCreateResponse;
import com.chil.ticketingservice.domain.user.dto.response.UserGetResponse;
import com.chil.ticketingservice.domain.user.dto.response.UserLoginResponse;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.enums.UserRole;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("회원가입 성공")
    void createUser_Success() {
        // given
        UserCreateRequest request = new UserCreateRequest(
                "test@test.com",
                "testUser",
                LocalDate.of(1990, 1, 1),
                "password123"
        );

        // when
        UserCreateResponse response = userService.createUser(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotBlank();

        User savedUser = userRepository.findByEmail(request.email()).orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo(request.email());
        assertThat(savedUser.getUsername()).isEqualTo(request.username());
        assertThat(savedUser.getBirth()).isEqualTo(request.birth());
        assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);
        assertThat(passwordEncoder.matches(request.password(), savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void createUser_Fail_EmailExists() {
        // given
        User existingUser = new User(
                "test@test.com",
                "existingUser",
                LocalDate.of(1990, 1, 1),
                passwordEncoder.encode("password123"),
                UserRole.USER
        );
        userRepository.save(existingUser);

        UserCreateRequest request = new UserCreateRequest(
                "test@test.com",
                "newUser",
                LocalDate.of(1995, 5, 5),
                "password456"
        );

        // when & then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.EMAIL_EXIST);
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        String rawPassword = "password123";
        User user = new User(
                "test@test.com",
                "testUser",
                LocalDate.of(1990, 1, 1),
                passwordEncoder.encode(rawPassword),
                UserRole.USER
        );
        userRepository.save(user);

        UserLoginRequest request = new UserLoginRequest(
                "test@test.com",
                rawPassword
        );

        // when
        UserLoginResponse response = userService.login(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 실패 - 사용자를 찾을 수 없음")
    void login_Fail_UserNotFound() {
        // given
        UserLoginRequest request = new UserLoginRequest(
                "notexist@test.com",
                "password123"
        );

        // when & then
        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.LOGIN_FAILED);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_WrongPassword() {
        // given
        String rawPassword = "password123";
        User user = new User(
                "test@test.com",
                "testUser",
                LocalDate.of(1990, 1, 1),
                passwordEncoder.encode(rawPassword),
                UserRole.USER
        );
        userRepository.save(user);

        UserLoginRequest request = new UserLoginRequest(
                "test@test.com",
                "wrongPassword"
        );

        // when & then
        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.LOGIN_FAILED);
    }

    @Test
    @DisplayName("회원 조회 성공")
    void getUser_Success() {
        // given
        User user = new User(
                "test@test.com",
                "testUser",
                LocalDate.of(1990, 1, 1),
                passwordEncoder.encode("password123"),
                UserRole.USER
        );
        User savedUser = userRepository.save(user);

        // when
        UserGetResponse response = userService.getUser(savedUser.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(savedUser.getId());
        assertThat(response.email()).isEqualTo(savedUser.getEmail());
        assertThat(response.username()).isEqualTo(savedUser.getUsername());
        assertThat(response.birth()).isEqualTo(savedUser.getBirth());
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("회원 조회 실패 - 사용자를 찾을 수 없음")
    void getUser_Fail_UserNotFound() {
        // given
        Long nonExistentUserId = Long.MAX_VALUE;

        // when & then
        assertThatThrownBy(() -> userService.getUser(nonExistentUserId))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("exceptionCode", ExceptionCode.USER_NOT_FOUND);
    }
}
