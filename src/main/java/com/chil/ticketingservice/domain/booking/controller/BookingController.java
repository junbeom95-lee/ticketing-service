package com.chil.ticketingservice.domain.booking.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
import com.chil.ticketingservice.domain.booking.dto.response.BookingResponse;
import com.chil.ticketingservice.domain.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<CommonResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingCreateRequest request
    ) {
        // TODO: Spring Security 구현 후 SecurityContext에서 userId 가져오기
        Long userId = 1L; // 임시 userId

        BookingResponse response = bookingService.createBooking(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(SuccessMessage.BOOKING_CREATE_SUCCESS, response));
    }

    @PutMapping("/{bookingId}/cancellation")
    public ResponseEntity<CommonResponse<BookingResponse>> cancelBooking(
            @PathVariable Long bookingId
    ) {
        BookingResponse response = bookingService.cancelBooking(bookingId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(SuccessMessage.BOOKING_CANCEL_SUCCESS, response));
    }
}
