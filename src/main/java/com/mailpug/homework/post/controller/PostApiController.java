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

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> addPost(@Valid @RequestBody PostDto postDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        Long result = postService.addPost(postDto, userId);

        return createApiResponseEntity(result, SuccessCode.INSERT_SUCCESS);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDto>> getPost(@PathVariable Long postId) {

        PostDto result = postService.getPost(postId);

        return createApiResponseEntity(result, SuccessCode.SELECT_SUCCESS);
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
