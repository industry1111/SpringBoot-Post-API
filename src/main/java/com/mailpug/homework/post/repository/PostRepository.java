package com.mailpug.homework.post.repository;

import com.mailpug.homework.common.dto.PageRequestDto;
import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, QuerydslPredicateExecutor<Post>, CustomPostRepository {

}
