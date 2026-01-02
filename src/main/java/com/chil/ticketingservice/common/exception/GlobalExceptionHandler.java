package com.chil.ticketingservice.common.exception;

import com.chil.ticketingservice.common.dto.CommonResponse;
import com.chil.ticketingservice.common.enums.ExceptionCode;
import com.chil.ticketingservice.domain.price.enums.SeatTypeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    //커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<Void>> customException(CustomException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();

        CommonResponse<Void> response = CommonResponse.exception(exceptionCode.getMessage());

        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> enumParseException(
            HttpMessageNotReadableException e
    ) {
        Throwable cause = e.getCause();

        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {

            Class<?> targetType = ife.getTargetType();

            // SeatTypeEnum 파싱 실패만 처리
            if (targetType == SeatTypeEnum.class) {

                ExceptionCode exceptionCode = ExceptionCode.SHOW_INVALID_SEAT_TYPE;

                CommonResponse<Void> response = CommonResponse.exception(exceptionCode.getMessage());

                return ResponseEntity
                        .status(exceptionCode.getStatus())
                        .body(response);
            }
        }

        // 그 외 JSON 파싱 오류
        CommonResponse<Void> response =
                CommonResponse.exception("잘못된 요청 형식입니다.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    //Valid 검증 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        CommonResponse<Void> response = CommonResponse.exception(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
