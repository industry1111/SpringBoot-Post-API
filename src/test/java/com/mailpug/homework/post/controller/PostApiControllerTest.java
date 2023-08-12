package com.mailpug.homework.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailpug.homework.common.codes.ErrorCode;
import com.mailpug.homework.config.exception.BusinessExceptionHandler;
import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;
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
            PostDto postDto = PostDto.builder()
                    .category("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글내용")
                    .build();

            String xUserId = "user1";

            Post mockPost = Post.builder()
                            .category("SpringBoot")
                            .title("게시글 생성")
                            .content("게시글내용")
                            .author(xUserId)
                            .build();

            when(postService.addPost(any(PostDto.class), anyString())).thenReturn(mockPost.getId());

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
            PostDto postDto = PostDto.builder()
                    .category("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글내용")
                    .build();

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.error").value("X-USERID 는 필수 입니다."));
        }

        @DisplayName("실패 - X-USERID 유효성")
        @Test
        void xUserIdInvalid() throws Exception{
            //given
            PostDto postDto = PostDto.builder()
                    .category("SpringBoot")
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
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.error").value("X-UERID는 3자에서 10자 사이여야 합니다."));
        }

        @DisplayName("실패 - 게시글 제목 null")
        @Test
        void postTitleIsNull() throws Exception{
            //given
            PostDto postDto = PostDto.builder()
                    .category("SpringBoot")
                    .title("")
                    .content("게시글내용")
                    .build();

            String xUserId = "user1";

            //when
            //then
            mockMvc.perform(post("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto))
                            .header("X-USERID", xUserId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.error").value("게시글 제목은 1~100자 사이로 작성해 주세요."));
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
            PostDto updatePostDto = PostDto.builder()
                    .id(1L)
                    .category("Spring")
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

            PostDto updatePostDto = PostDto.builder()
                    .category("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글 내용")
                    .build();
            //when
            //then
            mockMvc.perform(patch("/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatePostDto))
                            .header("X-USERID", "user2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("게시글 번호는 필수 입니다."));
        }

        @DisplayName("실패 - 작성자 수정자 다름")
        @Test
        void mismatchXUerId() throws Exception{
            //given
            Post post = Post.builder()
                    .category("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글 내용")
                    .author("user1")
                    .build();

            PostDto updatePostDto = PostDto.builder()
                    .id(1L)
                    .category("SpringBoot")
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


}