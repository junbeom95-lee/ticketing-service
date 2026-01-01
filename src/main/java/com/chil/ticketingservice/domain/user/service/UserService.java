package com.chil.ticketingservice.domain.user.service;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.user.dto.request.UserCreateRequest;
import com.chil.ticketingservice.domain.user.dto.response.UserCreateResponse;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.enums.UserRole;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserCreateResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ExceptionCode.EXISTS_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getEmail(),
                request.getUsername(),
                request.getBirth(),
                encodedPassword,
                UserRole.USER
        );

        User savedUser = userRepository.save(user);

        return UserCreateResponse.from(savedUser);
    }
}
