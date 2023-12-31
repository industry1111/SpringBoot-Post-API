package com.mailpug.homework.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailpug.homework.common.codes.ErrorCode;
import com.mailpug.homework.config.exception.BusinessExceptionHandler;
import com.mailpug.homework.post.entity.Post;
import com.mailpug.homework.post.dto.request.CreatePostDto;
import com.mailpug.homework.post.dto.request.UpdatePostDto;
import com.mailpug.homework.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(PostApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 등록")
    class addPostControllerTest {

        @DisplayName("성공")
        @Test
        void success() throws Exception {
            //given
            CreatePostDto postDto = CreatePostDto.builder()
                    .name("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글내용")
                    .build();

            String xUserId = "user1";

            Post post = Post.builder()
                            .name("SpringBoot")
                            .title("게시글 생성")
                            .content("게시글내용")
                            .author(xUserId)
                            .build();

            when(postService.addPost(any(CreatePostDto.class), anyString())).thenReturn(post.getId());

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto))
                            .header("X-USERID",xUserId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultMsg").value("INSERT SUCCESS"));
        }

        @DisplayName("실패 - X-USERID 값 null")
        @Test
        void xUserIdIsNull() throws Exception {
            //given
            CreatePostDto postDto = CreatePostDto.builder()
                    .name("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글내용")
                    .build();

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("X-USERID 값은 필수 입니다."));
        }

        @DisplayName("실패 - X-USERID 유효성")
        @Test
        void xUserIdInvalid() throws Exception{
            //given
            CreatePostDto postDto = CreatePostDto.builder()
                    .name("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글내용")
                    .build();

            String xUserId = "uuser1asduwqrqrq";

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto))
                            .header("X-USERID", xUserId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("X-UERID는 3자에서 10자 사이여야 합니다."));
        }

        @DisplayName("실패 - 게시글 카테고리 null")
        @Test
        void postNameIsNull() throws Exception{
            //given
            CreatePostDto postDto = CreatePostDto.builder()
                    .title("게시글 제목")
                    .content("게시글내용")
                    .build();

            String xUserId = "user1";

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto))
                            .header("X-USERID", xUserId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("게시글의 카테고리는 필수 값 입니다."));
        }

        @DisplayName("실패 - 게시글 제목 길이")
        @Test
        void postTitleIsEmpty() throws Exception{
            //given
            String title = "게시글제목";
            title = title.repeat(21);
            CreatePostDto postDto = CreatePostDto.builder()
                    .name("SpringBoot")
                    .title(title)
                    .content("게시글내용")
                    .build();

            String xUserId = "user1";

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto))
                            .header("X-USERID", xUserId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("게시글 제목은 1~100자 사이로 작성해 주세요."));
        }

        @DisplayName("실패 - 게시글 제목 길이")
        @Test
        void postTitleIsNull() throws Exception{
            //given
            CreatePostDto postDto = CreatePostDto.builder()
                    .name("SpringBoot")
                    .content("게시글내용")
                    .build();

            String xUserId = "user1";

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto))
                            .header("X-USERID", xUserId))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("게시글의 제목은 필수 값 입니다."));
        }

    }

    @Nested
    @DisplayName("게시글 수정")
    class modifyPostControllerTest {

        @DisplayName("성공")
        @Test
        void success() throws Exception {
            //given
            String modifiedText = "텍스트 수정";
            UpdatePostDto updatePostDto = UpdatePostDto.builder()
                    .id(1L)
                    .name("Spring")
                    .title("타이틀 변경")
                    .content(modifiedText)
                    .build();

            String xUserId = "user1";

            when(postService.modifyPost(any(),anyString()))
                    .thenReturn(1L);


            //when
            //then
            mockMvc.perform(patch("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatePostDto))
                            .header("X-USERID",xUserId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultMsg").value("UPDATE SUCCESS"));
        }

        @DisplayName("실패 - 게시글번호 null")
        @Test
        void postIdIsNull() throws Exception {
            //given

            UpdatePostDto updatePostDto = UpdatePostDto.builder()
                    .name("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글 내용")
                    .build();
            //when
            //then
            mockMvc.perform(patch("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatePostDto))
                            .header("X-USERID", "user2"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("게시글 번호는 필수 값 입니다."));
        }

        @DisplayName("실패 - 작성자 수정자 다름")
        @Test
        void mismatchXUerId() throws Exception{
            //given
            Post post = Post.builder()
                    .name("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글 내용")
                    .author("user1")
                    .build();

            UpdatePostDto updatePostDto = UpdatePostDto.builder()
                    .id(1L)
                    .name("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글 내용")
                    .build();

            when(postService.modifyPost(any(), anyString()))
                    .thenThrow(new BusinessExceptionHandler("작성자와 수정자가 다릅니다.", ErrorCode.FORBIDDEN_ERROR));

            //when
            //then
            mockMvc.perform(patch("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatePostDto))
                            .header("X-USERID", "user2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("작성자와 수정자가 다릅니다."));
        }

    }

    @DisplayName("게시글 삭제")
    @Test
    void deletePost() throws Exception {
        //given
        Long postId = 1L;
        String xUserId = "user1";

        //when
        //then
        mockMvc.perform(delete("/posts/"+postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postId))
                .header("X-USERID", xUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMsg").value("DELETE SUCCESS"));
    }

    @DisplayName("게시글 목록 조회")
    @Test
    void getPostList() throws Exception {
        //given
        String param = "page=1&size=5&keyword='카테고리'";

        //when
        //then
        mockMvc.perform(get("/posts?"+param)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMsg").value("SELECT SUCCESS"));
    }


}