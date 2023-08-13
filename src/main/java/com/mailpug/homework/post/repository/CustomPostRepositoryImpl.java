package com.mailpug.homework.post.repository;

import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;
import com.mailpug.homework.post.QPost;
import com.mailpug.homework.post.QPostDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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
    public Page<PostDto> getPostList(String keyword, Pageable pageable) {


        List<PostDto> result = queryFactory.select(
                        new QPostDto(
                                post.id,
                                post.category,
                                post.title,
                                post.content,
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

        return keyword != null && !keyword.isEmpty() ? post.category.eq(keyword) : null;
    }
}
