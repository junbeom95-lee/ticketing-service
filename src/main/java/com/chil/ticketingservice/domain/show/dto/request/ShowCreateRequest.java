package com.chil.ticketingservice.domain.show.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShowCreateRequest {

    @NotBlank(message = "공연 제목이 입력되지 않았습니다.")
    private String title;

    @NotBlank(message = "장소 입력이 되지 않았습니다.")
    private String location;

    @NotNull(message = "공연 일정이 입력되지 않았습니다.")
    /*@Pattern(
            regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d$",
            message = "날짜 형식은 yyyy-MM-dd HH:mm 입니다."
    )*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showDate;

    @NotNull(message = "관람등급이 입력되지 않았습니다.")
    private Long ageRating;

    @NotBlank(message = "공연 설명이 입력되지 않았습니다.")
    @Size(max = 255)
    private String description;

    private String imageUrl;
}
