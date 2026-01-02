package com.chil.ticketingservice.domain.booking.controller;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.booking.dto.request.BookingCreateRequest;
import com.chil.ticketingservice.domain.booking.dto.response.BookingCancelResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingCreateResponse;
import com.chil.ticketingservice.domain.booking.dto.response.BookingDetailResponse;
import com.chil.ticketingservice.domain.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<CommonResponse<BookingCreateResponse>> createBooking(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody BookingCreateRequest request
    ) {
        BookingCreateResponse response = bookingService.createBooking(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(SuccessMessage.BOOKING_CREATE_SUCCESS, response));
    }

    @PutMapping("/{bookingId}/cancellation")
    public ResponseEntity<CommonResponse<BookingCancelResponse>> cancelBooking(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long bookingId
    ) {
        BookingCancelResponse response = bookingService.cancelBooking(userId, bookingId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(SuccessMessage.BOOKING_CANCEL_SUCCESS, response));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<CommonResponse<BookingDetailResponse>> getBookingDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long bookingId
    ) {
        BookingDetailResponse response = bookingService.getBookingDetail(userId, bookingId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(SuccessMessage.BOOKING_GET_SUCCESS, response));
    }
}
