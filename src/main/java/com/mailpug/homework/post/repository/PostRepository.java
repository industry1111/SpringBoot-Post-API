package com.mailpug.homework.post.repository;

import com.mailpug.homework.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, QuerydslPredicateExecutor<Post>, CustomPostRepository {

}
