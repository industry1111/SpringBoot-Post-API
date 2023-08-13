package com.mailpug.homework.post.repository;

import com.mailpug.homework.post.dto.response.ResponsePostListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<ResponsePostListDto> getPostList(String keyword, Pageable pageable);
}
