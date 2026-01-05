package com.chil.ticketingservice.common.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,    // 현재 페이지의 실제 데이터
        int page,   // 현재 페이지 번호
        int size,   // 페이지 크기
        long totalElements, // 전체 데이터 개수
        int totalPages  // 전체 페이지 개수
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
