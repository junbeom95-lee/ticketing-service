package com.chil.ticketingservice.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserCreateRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Size(max = 50, message = "이름은 최대 50자여야 합니다.")
        String username,

        @NotNull(message = "생년월일은 필수 입력값입니다.")
        @Past(message = "생년월일은 과거 날짜여야 합니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birth,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Size(max = 20, message = "비밀번호는 최대 20자여야 합니다.")
        String password) {}