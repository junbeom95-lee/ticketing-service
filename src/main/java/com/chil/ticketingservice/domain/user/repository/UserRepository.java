package com.chil.ticketingservice.domain.user.repository;

import com.chil.ticketingservice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
