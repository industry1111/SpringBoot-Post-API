package com.mailpug.homework.post.repository;

import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;
import com.mailpug.homework.post.QPost;
import com.mailpug.homework.post.QPostDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    QPost post = QPost.post;

    @Override
    public List<PostDto> getPosts(String category, Pageable pageable) {


        return queryFactory.select(
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
                .where(validatedCategory(category))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }

    //category null,isEmpty 검사
    private BooleanExpression validatedCategory(String category) {
        if (category == null || category.isEmpty()) return null;

        return post.category.eq(category);
    }
}
