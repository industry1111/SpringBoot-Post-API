package com.mailpug.homework.post.service;

import com.mailpug.homework.common.codes.ErrorCode;
import com.mailpug.homework.config.exception.BusinessExceptionHandler;
import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;

import com.mailpug.homework.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;


    @DisplayName("게시글 등록")
    @Test
    void addPostTest() {

        //given
        String userId = "user1";
        PostDto postDto = PostDto.builder()
                .category("JAVA")
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        //when
        //then
        assertThatNoException().isThrownBy(() -> postService.addPost(postDto, userId));
    }

    @Nested
    @DisplayName("게시글 조회")
    class getPost {
        @DisplayName("성공")
        @Test
        void success() {
            //given
            Post post = Post.builder()
                    .category("category")
                    .title("title")
                    .content("content")
                    .author("user1")
                    .build();
            when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

            //when
            PostDto findPostDto = postService.getPost(1L);

            //then
            assertThat(findPostDto.getId()).isEqualTo(post.getId());
            assertThat(findPostDto.getContent()).isEqualTo(post.getContent());
        }

        @DisplayName("실패")
        @Test
        void fail() {
            //given
            //when
            //then
            assertThatThrownBy(() -> postService.getPost(1L)).isExactlyInstanceOf(BusinessExceptionHandler.class);
        }
    }

}