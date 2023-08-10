package com.mailpug.homework.post.repository;

import com.mailpug.homework.post.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    Post post;

    @BeforeEach
    void init() {
        post = Post.builder()
                .category("SpringBoot")
                .title("게시글 생성")
                .content("게시글내용")
                .createBy("user1")
                .build();

        postRepository.save(post);
    }


    @Nested
    @DisplayName("게시글 찾기")
    class findPostTest {

        @DisplayName("성공")
        @Test
        void success() {
            //given
            Long expected = 1L;

            //when
            Post findPost = postRepository.findById(expected).get();

            //then
            assertThat(findPost.getId()).isEqualTo(expected);
        }

        @DisplayName("결과 없음")
        @Test
        void isEmpty() {
            //given && when
            boolean result = postRepository.existsById(99L);
            
            //then
            assertThat(result).isFalse();
        }
    }

}