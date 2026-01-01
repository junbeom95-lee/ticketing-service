package com.chil.ticketingservice.domain.user.service;

import com.chil.ticketingservice.domain.user.dto.request.UserCreateRequest;
import com.chil.ticketingservice.domain.user.dto.response.UserCreateResponse;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public UserCreateResponse createUser(UserCreateRequest request) {

        User user = new User(
                request.getEmail(),
                request.getUsername(),
                request.getBirth(),
                request.getPassword()
        );

        User savedUser = userRepository.save(user);

        return UserCreateResponse.from(savedUser);
    }
}
