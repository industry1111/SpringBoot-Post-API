package com.mailpug.homework.post.repository;

import com.mailpug.homework.post.PostDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPostRepository {
    List<PostDto> getPosts(String keyword, Pageable pageable);
}
