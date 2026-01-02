package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.common.exception.CustomException;
import com.chil.ticketingservice.domain.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long>, ShowCustomRepository {

    default Show findShowById(Long id) {
        return findById(id).orElseThrow(
                () -> new CustomException(ExceptionCode.SHOW_NOT_FOUND)
        );
    }
}
