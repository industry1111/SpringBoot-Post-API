package com.mailpug.homework.post.repository;

import com.mailpug.homework.post.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<PostDto> getPostList(String keyword, Pageable pageable);
}
