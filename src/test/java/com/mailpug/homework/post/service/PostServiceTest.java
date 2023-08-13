package com.mailpug.homework.post.service;

import com.mailpug.homework.common.dto.PageRequestDto;
import com.mailpug.homework.common.dto.PageResultDto;
import com.mailpug.homework.config.exception.BusinessExceptionHandler;
import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.dto.request.CreatePostDto;

import com.mailpug.homework.post.dto.request.UpdatePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostListDto;
import com.mailpug.homework.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
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
        CreatePostDto postDto = CreatePostDto.builder()
                .name("JAVA")
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
                    .name("name")
                    .title("title")
                    .content("content")
                    .author("user1")
                    .build();
            when(postRepository.findById(anyLong()))
                    .thenReturn(Optional.of(post));

            //when
            ResponsePostDto findPostDto = postService.getPost(1L);

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
                    .hasMessage("데이터가 존재하지 않습니다.");
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
                    .name("name")
                    .title("title")
                    .content("content")
                    .author("user1")
                    .build();

            String modifiedText = "내용 수정";
            UpdatePostDto postDto = UpdatePostDto.builder()
                    .id(1L)
                    .name("Junit")
                    .title("수정된 제목")
                    .content(modifiedText)
                    .build();

            when(postRepository.findById(anyLong()))
                    .thenReturn(Optional.of(post));

            //when
            postService.modifyPost(postDto,"user1");

            //then
            assertThat(post.getContent()).isEqualTo(modifiedText);
        }

        @DisplayName("실패 - 작성자와 수정자 다름")
        @Test
        void forbiddenFail() {
            //given
            Post post = Post.builder()
                    .name("name")
                    .title("title")
                    .content("content")
                    .author("user1")
                    .build();

            String modifiedText = "내용 수정";
            UpdatePostDto postDto = UpdatePostDto.builder()
                    .id(1L)
                    .name("Junit")
                    .title("수정된 제목")
                    .content(modifiedText)
                    .build();

            when(postRepository.findById(anyLong()))
                    .thenReturn(Optional.of(post));

            //when
            //then
            assertThatThrownBy(() -> postService.modifyPost(postDto,"user2"))
                    .isExactlyInstanceOf(BusinessExceptionHandler.class)
                    .hasMessage("권한이 없습니다.");
        }

        @DisplayName("실패 - 존재하지 않는 게시글")
        @Test
        void notFoundPost() {
            //given
            Post post = Post.builder()
                    .name("name")
                    .title("title")
                    .content("content")
                    .author("user1")
                    .build();

            String modifiedText = "내용 수정";
            UpdatePostDto postDto = UpdatePostDto.builder()
                    .id(2L)
                    .name("Junit")
                    .title("수정된 제목")
                    .content(modifiedText)
                    .build();

            //when
            //then
            assertThatThrownBy(() -> postService.modifyPost(postDto,"user1"))
                    .isExactlyInstanceOf(BusinessExceptionHandler.class)
                    .hasMessage("데이터가 존재하지 않습니다.");
        }
    }


    @DisplayName("게시글 삭제")
    @Test
    void deletePost() {
        //given
        Post post = Post.builder()
                .name("java")
                .title("게시글 제목")
                .content("게시글 내용")
                .author("user1")
                .build();


        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.of(post));

        //when
        //then
        assertThatThrownBy(() -> postService.deletePost(1L, "user2"))
                .isExactlyInstanceOf(BusinessExceptionHandler.class)
                .hasMessage("권한이 없습니다.");
    }

    @Nested
    @DisplayName("게시글 목록조회")
    class getPostListTest {

        @DisplayName("성공")
        @Test
        void success() {
            //given
            Pageable pageable = PageRequest.of(0,5, Sort.by("id"));
            List<ResponsePostListDto> responsePostDtoList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                ResponsePostListDto responsePostDto = ResponsePostListDto.builder()
                        .id((long) i)
                        .title("게시글 제목")
                        .author("user1")
                        .build();
                responsePostDtoList.add(responsePostDto);
            }



            //모의 객체의 결과는 검색된 결과는 20건 , 현재 페이지는 0 사이즈는 5이므로 가져간 데이터는 5건
            PageImpl<ResponsePostListDto> expectedResult = new PageImpl<>(responsePostDtoList, pageable, 20);

            when(postRepository.getPostList("카테고리", pageable))
                    .thenReturn(expectedResult);

            //when
            PageRequestDto pageRequestDto = PageRequestDto.builder()
                    .page(1)
                    .size(5)
                    .keyword("카테고리")
                    .build();

            PageResultDto<ResponsePostListDto> result = postService.getPostList(pageRequestDto);

            //then
            assertThat(result.getTotalPage()).isEqualTo(4);
            assertThat(result.getStart()).isEqualTo(1);
            assertThat(result.getEnd()).isEqualTo(4);

        }

        @DisplayName("실페 - 데이터 없음")
        @Test
        void noMatchesFound() {
            Pageable pageable = PageRequest.of(0,5, Sort.by("id"));

            PageRequestDto pageRequestDto = PageRequestDto.builder()
                    .page(1)
                    .size(5)
                    .keyword("카테고리")
                    .build();

            when(postRepository.getPostList("카테고리", pageable))
                    .thenReturn(new PageImpl<>(new ArrayList<>(),pageable,0));

            //when

            //then
            assertThatThrownBy(() -> postService.getPostList(pageRequestDto))
                    .isExactlyInstanceOf(BusinessExceptionHandler.class)
                    .hasMessage("데이터가 존재하지 않습니다.");
        }

    }


}