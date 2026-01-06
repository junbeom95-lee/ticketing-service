package com.chil.ticketingservice.domain.user.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.domain.user.dto.request.UserCreateRequest;
import com.chil.ticketingservice.domain.user.dto.request.UserLoginRequest;
import com.chil.ticketingservice.domain.user.dto.response.UserCreateResponse;
import com.chil.ticketingservice.domain.user.dto.response.UserGetResponse;
import com.chil.ticketingservice.domain.user.dto.response.UserLoginResponse;
import com.chil.ticketingservice.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.chil.ticketingservice.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/users")
    public ResponseEntity<CommonResponse<UserCreateResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {

        UserCreateResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(USER_CREATE_SUCCESS, response));
    }

    // 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse<UserLoginResponse>> login(@Valid @RequestBody UserLoginRequest request) {

        UserLoginResponse response = userService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(AUTH_LOGIN_SUCCESS, response));
    }

    // 회원 조회
    @GetMapping("/users")
    public ResponseEntity<CommonResponse<UserGetResponse>> getUser(@AuthenticationPrincipal Long userId) {

        UserGetResponse response = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_GET_SUCCESS, response));
    }
}
