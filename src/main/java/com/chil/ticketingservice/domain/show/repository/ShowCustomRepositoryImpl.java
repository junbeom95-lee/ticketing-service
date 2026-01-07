package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.show.dto.request.ShowSearchRequest;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.enums.KeywordTypeEnum;
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
import java.util.Optional;

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

        // keyword 기본값 보장 (null / blank 안전)
        KeywordTypeEnum keywordType = Optional.ofNullable(request.keyword())
                .filter(k -> !k.isBlank())
                .map(KeywordTypeEnum::from)
                .orElse(KeywordTypeEnum.LATEST);

        // 기본 조건
        // 현재 년도 기준 작년 공연은 조회 안됨
        LocalDateTime startOfYear = LocalDate.now()
                .withDayOfYear(1)
                .atStartOfDay();
        builder.and(show.showDate.goe(startOfYear));

        // 기본 정렬 (null 방지)
        OrderSpecifier<?> order = show.showDate.asc();
        boolean bookingJoin = false;

        switch (keywordType) {
            case UPCOMING -> { // 공연임박순
                // 현재 날짜 기준 7일 이내 공연
                LocalDateTime start = LocalDate.now().atStartOfDay();
                LocalDateTime end = LocalDate.now().plusDays(7).atTime(23, 59, 59);

                builder.and(show.showDate.between(start, end));

                order = show.showDate.asc();
            }

            case BESTSELLER -> { // 많이 팔린순
                bookingJoin = true;

                order = booking.countDistinct().desc();
            }

            case POPULAR -> { // 인기순
                order = like.id.countDistinct().desc();
            }

            case LATEST -> { // 최신순
                order = show.showDate.asc();
            }
        }

        JPAQuery<ShowResponse> query = jpaQueryFactory
                .select(Projections.constructor(
                        ShowResponse.class,
                        show.id,
                        show.title,
                        show.location,
                        show.showDate,
                        show.imageUrl,
                        like.id.countDistinct()
                ))
                .from(show)
                .leftJoin(like).on(like.show.id.eq(show.id));

        if (bookingJoin) {
            query.leftJoin(booking)
                    .on(booking.showId.eq(show.id).and(booking.isCanceled.ne(true)));
        }

        List<ShowResponse> content = query
                .where(builder)
                .groupBy(show.id)
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
