package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.show.dto.request.ShowSearchRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.chil.ticketingservice.domain.booking.entity.QBooking.booking;
import static com.chil.ticketingservice.domain.like.entity.QLike.like;
import static com.chil.ticketingservice.domain.show.entity.QShow.show;

@AllArgsConstructor
@Slf4j
public class ShowCustomRepositoryImpl implements ShowCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 공연 리스트 페이지 query dsl
    @Override
    public Page<ShowResponse> showSearch(ShowSearchRequest request, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // 제목 조건
        String title = request.showTitle();
        if (title != null && !title.isBlank()) {
            builder.and(show.title.contains(title));
        }

        // keyword 기본값 보장
        String keyword = request.keyword();
        if (keyword == null || keyword.isBlank()) {
            keyword = "latest";
        }

        // 기본 정렬
        OrderSpecifier<?> order = show.showDate.desc();
        boolean needBookingJoin = false;

        // 4. keyword 분기
        switch (keyword) {
            case "upcoming" -> {
                LocalDateTime start = LocalDate.now().atStartOfDay();
                LocalDateTime end = LocalDate.now().plusDays(7).atTime(23, 59, 59);

                builder.and(show.showDate.between(start, end));
            }

            case "bestseller" -> {
                needBookingJoin = true;
                order = booking.countDistinct().desc();
            }

            case "popular" -> {
                order = like.show.id.count().desc();
            }

            case "latest" -> {
                // 기본값 → 아무것도 안 함
            }
        }

        // 5. Query 생성
        JPAQuery<ShowResponse> query = jpaQueryFactory
                .select(Projections.constructor(
                        ShowResponse.class,
                        show.id,
                        show.title,
                        show.location,
                        show.showDate,
                        show.imageUrl,
                        like.countDistinct()
                ))
                .from(show)
                .leftJoin(like).on(like.show.id.eq(show.id));

        if (needBookingJoin) {
            query.leftJoin(booking).on(booking.show.id.eq(show.id));
        }

        // 6. 실행
        List<ShowResponse> content = query
                .where(builder)
                .groupBy(
                        show.id,
                        show.title,
                        show.location,
                        show.showDate,
                        show.imageUrl
                )
                .orderBy(order)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(show.count())
                .from(show)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}
