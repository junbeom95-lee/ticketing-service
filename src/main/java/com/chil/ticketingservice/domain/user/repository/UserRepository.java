package com.chil.ticketingservice.domain.user.repository;

import static com.chil.ticketingservice.common.enums.ExceptionCode.USER_NOT_FOUND;

import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default User findUserById(Long userId) {
        return findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
