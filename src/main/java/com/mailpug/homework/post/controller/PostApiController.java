package com.mailpug.homework.post.controller;

import com.mailpug.homework.common.codes.SuccessCode;
import com.mailpug.homework.common.dto.PageRequestDto;
import com.mailpug.homework.common.dto.PageResultDto;
import com.mailpug.homework.common.reponse.CustomApiResponse;
import com.mailpug.homework.common.reponse.ErrorResponse;
import com.mailpug.homework.post.dto.CreatePostDto;
import com.mailpug.homework.post.dto.PostDto;
import com.mailpug.homework.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;


@Api(tags = "게시판 CRUD API")
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
    @Operation(summary = "게시글 등록", description = "게시글을 등록 합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message ="INSERT SUCCESS"),
            @ApiResponse(code =400, message = "잘못된 요청 입니다.", response = ErrorResponse.class)})
    @PostMapping
    public ResponseEntity<CustomApiResponse<Long>> addPost(@Valid @RequestBody CreatePostDto createPostDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        Long result = postService.addPost(createPostDto, userId);

        return createApiResponseEntity(result, SuccessCode.INSERT_SUCCESS);
    }

    /**
     * [API] 게시글 단건 조회
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 조회한 게시글 결과 및 응답 코드 반환
     */
    @GetMapping("/{postId}")
    public ResponseEntity<CustomApiResponse<PostDto>> getPost(@PathVariable Long postId) {

        PostDto result = postService.getPost(postId);

        return createApiResponseEntity(result, SuccessCode.SELECT_SUCCESS);
    }

    /**
     * [API] 게시글 수정
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 수정된 게시글 번호 및 응답 코드 반환
     */
    @PatchMapping
    public ResponseEntity<CustomApiResponse<Long>> modifyPost(@Valid @RequestBody PostDto postDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

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
    public ResponseEntity<CustomApiResponse<Long>> deletePost(@PathVariable Long postId, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        postService.deletePost(postId, userId);

        return createApiResponseEntity(postId,SuccessCode.DELETE_SUCCESS);
    }

    /**
     * [API] 게시글 조회
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 삭제된 게시글 번호 및 응답 코드 반환
     */
    @GetMapping
    public ResponseEntity<CustomApiResponse<PageResultDto<PostDto>>> getPostList(@RequestBody PageRequestDto pageRequestDto) {

        PageResultDto<PostDto> result = postService.getPostList(pageRequestDto);

        return createApiResponseEntity(result,SuccessCode.SELECT_SUCCESS);
    }


    /**
     * Service 에서 가져온 결과 값을 ResponseEntiTy<ApiResponse<T> 로 변환 해주는 메서드
     *
     * @return ResponseEntity<ApiResponse<T>>
     */

    private <T> ResponseEntity<CustomApiResponse<T>> createApiResponseEntity(T result, SuccessCode successCode) {
        CustomApiResponse<T> customApiResponse = CustomApiResponse.<T>builder()
                .result(result)
                .resultCode(successCode.getStatus())
                .resultMsg(successCode.getMessage())
                .build();

        return new ResponseEntity<>(customApiResponse, HttpStatus.OK);
    }
}
