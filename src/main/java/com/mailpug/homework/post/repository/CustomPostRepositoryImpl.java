package com.mailpug.homework.post.repository;


import com.mailpug.homework.post.dto.response.QResponsePostListDto;
import com.mailpug.homework.post.dto.response.ResponsePostListDto;
import com.mailpug.homework.post.entity.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    QPost post = QPost.post;

    @Override
    public Page<ResponsePostListDto> getPostList(String keyword, Pageable pageable) {


        List<ResponsePostListDto> result = queryFactory.select(
                        new QResponsePostListDto(
                                post.id,
                                post.title,
                                post.author,
                                post.createAt,
                                post.updateAt
                        )
                )
                .from(post)
                .where(
                        postNameEq(keyword)
                )
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        postNameEq(keyword)
                ).fetchFirst();

        return new PageImpl<>(result, pageable, count);
    }

    //category null,isEmpty 검사
    private BooleanExpression postNameEq(String keyword) {

        return keyword != null && !keyword.isEmpty() ? post.name.eq(keyword) : null;
    }
}
