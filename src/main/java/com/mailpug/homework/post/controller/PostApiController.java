package com.mailpug.homework.post.controller;

import com.mailpug.homework.common.codes.SuccessCode;
import com.mailpug.homework.common.dto.PageRequestDto;
import com.mailpug.homework.common.dto.PageResultDto;
import com.mailpug.homework.common.reponse.CustomApiResponse;
import com.mailpug.homework.post.dto.request.CreatePostDto;
import com.mailpug.homework.post.dto.request.UpdatePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostListDto;
import com.mailpug.homework.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "게시글 등록", description = "게시글을 등록 합니다.\n + X-USERID는 3~10자 사이여야 합니다.")
    @ApiResponse(code = 201, message ="INSERT SUCCESS")
    @PostMapping
    public CustomApiResponse<Long> addPost(@Valid @RequestBody CreatePostDto createPostDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        Long result = postService.addPost(createPostDto, userId);


        return new CustomApiResponse<>(result,SuccessCode.INSERT_SUCCESS);

    }

    /**
     * [API] 게시글 단건 조회
     *
     * @return ResponseEntity<Object>: 조회한 게시글 결과 및 응답 코드 반환
     */
    @Operation(summary = "게시글 단건 조회", description = "입력한 게시글 번호에 대한 게시글 정보를 조회 합니다.")
    @ApiResponse(code = 200, message ="INSERT SUCCESS", response = CustomApiResponse.class)
    @GetMapping("/{postId}")
    public CustomApiResponse<ResponsePostDto> getPost(@PathVariable Long postId) {

        ResponsePostDto result = postService.getPost(postId);

        return new CustomApiResponse<>(result,SuccessCode.SELECT_SUCCESS);
    }

    /**
     * [API] 게시글 수정
     *
     * @return RResponseEntity<Object>: 수정된 게시글 번호 및 응답 코드 반환
     */

    @Operation(summary = "게시글 수정", description = "해당 게시글에 대한 정보를 수정합니다.\n + X-USERID는 3~10자 사이여야 합니다.")
    @ApiResponse(code = 200, message ="INSERT SUCCESS")
    @PatchMapping
    public CustomApiResponse<Long> modifyPost(@Valid @RequestBody UpdatePostDto updatePostDto, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        if (updatePostDto.getId() == null) {
            throw new IllegalArgumentException("게시글 번호는 필수 입니다.");
        }

        Long result = postService.modifyPost(updatePostDto, userId);

        return new CustomApiResponse<>(result,SuccessCode.UPDATE_SUCCESS);
    }

    /**
     * [API] 게시글 삭제
     *
     * @return ResponseEntity<ApiResponse<PostDto>>: 삭제된 게시글 번호 및 응답 코드 반환
     */
    @Operation(summary = "게시글 삭제", description = "해당 게시글을 삭제합니다.\n + X-USERID는 3~10자 사이여야 합니다.")
    @ApiResponse(code = 204, message ="INSERT SUCCESS")
    @DeleteMapping("{postId}")
    public CustomApiResponse<Long> deletePost(@PathVariable Long postId, @RequestHeader(name = "X-USERID") @Size(min=3,max = 10) String userId) {

        postService.deletePost(postId, userId);

        return new CustomApiResponse<>(postId,SuccessCode.DELETE_SUCCESS);
    }

    /**
     * [API] 게시글 목록 조회
     *
     * @return ResponseEntity<Object>: 삭제된 게시글 번호 및 응답 코드 반환
     */
    @Operation(summary = "게시글 목록 조회", description = "해당되는 카테고리의 게시글 목록을 조회합니다. - 빈 값일시 모든 게시글을 기준으로 조회")
    @ApiResponse(code = 200, message ="INSERT SUCCESS")
    @GetMapping
    public CustomApiResponse<PageResultDto<ResponsePostListDto>> getPostList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {

        PageRequestDto pageRequestDto = new PageRequestDto(page,size,keyword);

        PageResultDto<ResponsePostListDto> result = postService.getPostList(pageRequestDto);


        return new CustomApiResponse<>(result,SuccessCode.SELECT_SUCCESS);
    }
}
