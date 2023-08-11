package com.mailpug.homework.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class PostTest {

    @DisplayName("게시글 수정")
    @Test
    void updatePost() {

        //given
        Post post = Post.builder()
                .category("SpringBoot")
                .title("게시글 생성")
                .content("게시글내용")
                .author("user1")
                .build();

        //when
        String expected = "게시글 수정";
        post.changeContent(expected);

        //then
        assertThat(post.getContent()).isEqualTo(expected);
    }
}