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
            when(postRepository.findById(anyLong()))
                    .thenReturn(Optional.of(post));

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
            assertThatThrownBy(() -> postService.getPost(1L))
                    .isExactlyInstanceOf(BusinessExceptionHandler.class)
                    .hasMessage("존재하지 않는 게시글 번호 입니다.");
        }
    }


    @Nested
    @DisplayName("게시글 수정")
    class modifyPostTest {
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

            when(postRepository.findById(anyLong()))
                    .thenReturn(Optional.of(post));

            //when
            String modifiedText = "내용 수정";
            PostDto postDto = PostDto.builder()
                    .id(1L)
                    .category("Junit")
                    .title("수정된 제목")
                    .content(modifiedText)
                    .author("user1")
                    .build();
            postService.modifyPost(postDto,"user1");

            //then
            assertThat(post.getContent()).isEqualTo(modifiedText);
        }

        @DisplayName("실패 - 작성자와 수정자 다름")
        @Test
        void forbiddenFail() {
            //given
            Post post = Post.builder()
                    .category("category")
                    .title("title")
                    .content("content")
                    .author("user1")
                    .build();

            when(postRepository.findById(anyLong()))
                    .thenReturn(Optional.of(post));

            //when
            String modifiedText = "내용 수정";
            PostDto postDto = PostDto.builder()
                    .id(1L)
                    .category("Junit")
                    .title("수정된 제목")
                    .content(modifiedText)
                    .author("user1")
                    .build();


            //then
            assertThatThrownBy(() -> postService.modifyPost(postDto,"user2"))
                    .isExactlyInstanceOf(BusinessExceptionHandler.class)
                    .hasMessage("권한이 없습니다.");
        }

        @DisplayName("실패 - 존재하지 않는 게시글")
        @Test
        void notFoundFail() {
            //given
            Post post = Post.builder()
                    .category("category")
                    .title("title")
                    .content("content")
                    .author("user1")
                    .build();


            //when
            String modifiedText = "내용 수정";
            PostDto postDto = PostDto.builder()
                    .id(2L)
                    .category("Junit")
                    .title("수정된 제목")
                    .content(modifiedText)
                    .author("user1")
                    .build();

            //then
            assertThatThrownBy(() -> postService.modifyPost(postDto,"user1"))
                    .isExactlyInstanceOf(BusinessExceptionHandler.class)
                    .hasMessage("존재하지 않는 게시글 번호 입니다.");
        }
    }

}