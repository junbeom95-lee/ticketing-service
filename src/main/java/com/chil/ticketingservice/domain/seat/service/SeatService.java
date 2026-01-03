package com.chil.ticketingservice.domain.seat.service;

import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableResponse;
import com.chil.ticketingservice.domain.seat.dto.response.SeatAvailableTypeResponse;
import com.chil.ticketingservice.domain.seat.entity.Seat;
import com.chil.ticketingservice.domain.seat.enums.SeatTypeEnum;
import com.chil.ticketingservice.domain.seat.repository.SeatRepository;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    //좌석 생성
    @Transactional
    public void createSeat(Show show) {

        List<Seat> seatList = new ArrayList<>();

        seatList.addAll(createSeatList(show, SeatTypeEnum.VIP));
        seatList.addAll(createSeatList(show, SeatTypeEnum.S));
        seatList.addAll(createSeatList(show, SeatTypeEnum.A));
        seatList.addAll(createSeatList(show, SeatTypeEnum.B));

        seatRepository.saveAll(seatList);
    }

    //예매 가능한 좌석 목록 조회
    @Transactional(readOnly = true)
    public List<SeatAvailableResponse> getAvailableSeatList(Long showId, SeatTypeEnum seatType) {

        Show savedShow = showRepository.findShowById(showId);

        return seatRepository.findAllByShowAndSeatTypeAndSeatStatus(savedShow, seatType);
    }

    //좌석 등급별 남은 좌석 수 조회
    @Transactional(readOnly = true)
    public List<SeatAvailableTypeResponse> getAvailableSeatTypeList(Long showId) {

        Show savedShow = showRepository.findShowById(showId);

        return seatRepository.countByShow(savedShow);
    }

    //특정 show 좌석 리스트 생성
    private List<Seat> createSeatList(Show show, SeatTypeEnum seatType) {

        List<Seat> seatList = new ArrayList<>();

        for (int i = 1; i <= seatType.getSize(); i++) {
            Seat seat = new Seat(show, seatType, i);
            seatList.add(seat);
        }

        return seatList;
    }
}
