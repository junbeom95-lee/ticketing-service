package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.like.repository.LikeRepository;
import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
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
    public Page<ShowResponse> showSearch(String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        OrderSpecifier<?> orderSpecifier = null;

        boolean joinBooking = false;

        switch (keyword) {
            case "latest" :
                // 최신순
                orderSpecifier = show.showDate.desc(); // 기본값

                break;

            case "upcoming" :
                // 공연 임박순
                // 오늘 날짜 기준 1주일 이내의 공연만 출력

                orderSpecifier = show.showDate.desc(); // 기본값

                LocalDateTime start = LocalDate.now().atStartOfDay();
                LocalDateTime end = LocalDate.now().plusDays(7).atTime(23, 59, 59);

                builder.and(show.showDate.between(start, end));

                break;

            case "bestseller" : // 판매 많은 순
                joinBooking = true;

                orderSpecifier = booking.countDistinct().desc();

                break;

            case "popular" : // 인기순
                orderSpecifier = like.show.id.count().desc();

                break;
        }

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

        if (joinBooking) {
            query.leftJoin(booking).on(booking.show.id.eq(show.id));
        }

        List<ShowResponse> showResponseList = query
                .where(builder)
                .groupBy(
                        show.id,
                        show.title,
                        show.location,
                        show.showDate,
                        show.imageUrl
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(show.count())
                .from(show)
                .fetchOne();

        return new PageImpl<>(showResponseList, pageable, total == null ? 0L : total );
    }
}
