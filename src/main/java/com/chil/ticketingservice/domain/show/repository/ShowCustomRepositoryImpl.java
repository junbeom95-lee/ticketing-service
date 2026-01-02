package com.chil.ticketingservice.domain.show.repository;

import com.chil.ticketingservice.domain.show.dto.response.ShowResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.chil.ticketingservice.domain.show.entity.QShow.show;

@AllArgsConstructor
public class ShowCustomRepositoryImpl implements ShowCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 공연 리스트 페이지 query dsl
    @Override
    public Page<ShowResponse> showSearch(Pageable pageable) {
        List<ShowResponse> showResponseList = jpaQueryFactory
                .select(Projections.constructor(
                        ShowResponse.class,
                        show.id,
                        show.title,
                        show.location,
                        show.showDate,
                        show.imageUrl,
                        Expressions.constant(0L)
                ))
                .from(show)
                .orderBy(show.showDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(show.count())
                .from(show)
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(showResponseList, pageable, total);
    }
}
