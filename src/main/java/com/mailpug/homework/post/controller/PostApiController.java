package com.mailpug.homework.post.controller;

import com.mailpug.homework.common.codes.SuccessCode;
import com.mailpug.homework.common.dto.PageRequestDto;
import com.mailpug.homework.common.dto.PageResultDto;
import com.mailpug.homework.common.reponse.CustomApiResponse;
import com.mailpug.homework.common.reponse.ErrorResponse;
import com.mailpug.homework.post.dto.request.CreatePostDto;
import com.mailpug.homework.post.dto.request.UpdatePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostListDto;
import com.mailpug.homework.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
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
     * @return ResponseEntity<Object>: 생성된 게시글 번호 및 응답 코드 반환
     */
    @Operation(summary = "게시글 등록", description = "게시글을 등록 합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message ="INSERT SUCCESS"),
            @ApiResponse(code =400, message = "잘못된 요청 입니다.", response = ErrorResponse.class)})
    @PostMapping
    public ResponseEntity<Object> addPost(@Valid @RequestBody CreatePostDto createPostDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        Long result = postService.addPost(createPostDto, userId);

        return createApiResponseEntity(result, SuccessCode.INSERT_SUCCESS);
    }

    /**
     * [API] 게시글 단건 조회
     *
     * @return ResponseEntity<Object>: 조회한 게시글 결과 및 응답 코드 반환
     */
    @Operation(summary = "게시글 단건 조회", description = "입력한 게시글 번호에 대한 게시글 정보를 조회 합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable Long postId) {

        ResponsePostDto result = postService.getPost(postId);

        return createApiResponseEntity(result, SuccessCode.SELECT_SUCCESS);
    }

    /**
     * [API] 게시글 수정
     *
     * @return RResponseEntity<Object>: 수정된 게시글 번호 및 응답 코드 반환
     */
    @PatchMapping
    public ResponseEntity<Object> modifyPost(@Valid @RequestBody UpdatePostDto updatePostDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        if (updatePostDto.getId() == null) {
            throw new IllegalArgumentException("게시글 번호는 필수 입니다.");
        }

        Long result = postService.modifyPost(updatePostDto, userId);

        return createApiResponseEntity(result, SuccessCode.UPDATE_SUCCESS);
    }

    /**
     * [API] 게시글 삭제
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 삭제된 게시글 번호 및 응답 코드 반환
     */
    @DeleteMapping("{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable Long postId, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        postService.deletePost(postId, userId);

        return createApiResponseEntity(postId,SuccessCode.DELETE_SUCCESS);
    }

    /**
     * [API] 게시글 목록 조회
     *
     * @return ResponseEntity<Object>: 삭제된 게시글 번호 및 응답 코드 반환
     */
    @GetMapping
    public ResponseEntity<Object> getPostList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {

        PageRequestDto pageRequestDto = new PageRequestDto(page,size,keyword);

        PageResultDto<ResponsePostListDto> result = postService.getPostList(pageRequestDto);


        return createApiResponseEntity(result,SuccessCode.SELECT_SUCCESS);
    }


    /**
     * Service 에서 가져온 결과 값을 ResponseEntity<Object> 로 변환 해주는 메서드
     *
     * @return ResponseEntity<Object>
     */

    private <T> ResponseEntity<Object> createApiResponseEntity(T result, SuccessCode successCode) {
        CustomApiResponse<T> customApiResponse = CustomApiResponse.<T>builder()
                .result(result)
                .resultCode(successCode.getStatus())
                .resultMsg(successCode.getMessage())
                .build();

        return ResponseEntity.ok(customApiResponse);
    }
}
