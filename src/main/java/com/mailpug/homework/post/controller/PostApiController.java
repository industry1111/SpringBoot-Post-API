package com.mailpug.homework.post.controller;

import com.mailpug.homework.common.codes.SuccessCode;
import com.mailpug.homework.common.reponse.ApiResponse;
import com.mailpug.homework.post.PostDto;
import com.mailpug.homework.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostApiController {

    private final PostService postService;

    /**
     * [API] 게시글 등록
     *
     * @return ResponseEntity<ApiResponse<Long>>: 생성된 게시글 번호 및 응답 코드 반환
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> addPost(@Valid @RequestBody PostDto postDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        Long result = postService.addPost(postDto, userId);

        return createApiResponseEntity(result, SuccessCode.INSERT_SUCCESS);
    }

    /**
     * [API] 게시글 단건 조회
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 조회한 게시글 결과 및 응답 코드 반환
     */
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDto>> getPost(@PathVariable Long postId) {

        PostDto result = postService.getPost(postId);

        return createApiResponseEntity(result, SuccessCode.SELECT_SUCCESS);
    }

    /**
     * [API] 게시글 수정
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 수정된 게시글 번호 및 응답 코드 반환
     */
    @PatchMapping
    public ResponseEntity<ApiResponse<Long>> modifyPost(@Valid @RequestBody PostDto postDto,  @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        if (postDto.getId() == null) {
            throw new IllegalArgumentException("게시글 번호는 필수 입니다.");
        }

        Long result = postService.modifyPost(postDto, userId);

        return createApiResponseEntity(result, SuccessCode.UPDATE_SUCCESS);
    }

    /**
     * [API] 게시글 삭제
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 삭제된 게시글 번호 및 응답 코드 반환
     */
    @DeleteMapping("{postId}")
    public ResponseEntity<ApiResponse<Long>> deletePost(@PathVariable Long postId, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        postService.deletePost(postId, userId);

        return createApiResponseEntity(postId,SuccessCode.DELETE_SUCCESS);
    }

    private <T> ResponseEntity<ApiResponse<T>> createApiResponseEntity(T result, SuccessCode successCode) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .result(result)
                .resultCode(successCode.getStatus())
                .resultMsg(successCode.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
