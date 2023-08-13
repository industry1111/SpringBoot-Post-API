package com.mailpug.homework.post;

import com.mailpug.homework.post.dto.request.UpdatePostDto;
import com.mailpug.homework.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class PostTest {

    @DisplayName("게시글 수정")
    @Test
    void updatePost() {

        //given
        Post post = Post.builder()
                .name("SpringBoot")
                .title("게시글 생성")
                .content("게시글내용")
                .author("user1")
                .build();

        //when
        UpdatePostDto updatePostDto = UpdatePostDto.builder()
                .name("Spring")
                .title("게시글 수정")
                .content("게시글 내용 수정")
                .build();

        post.updatePost(updatePostDto);

        //then
        assertThat(post.getContent()).isEqualTo("게시글 내용 수정");
    }
}