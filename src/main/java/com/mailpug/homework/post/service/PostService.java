package com.mailpug.homework.post.service;

import com.mailpug.homework.common.codes.ErrorCode;
import com.mailpug.homework.common.dto.PageRequestDto;
import com.mailpug.homework.common.dto.PageResultDto;
import com.mailpug.homework.config.exception.BusinessExceptionHandler;
import com.mailpug.homework.post.entity.Post;
import com.mailpug.homework.post.dto.request.CreatePostDto;
import com.mailpug.homework.post.dto.request.UpdatePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostDto;
import com.mailpug.homework.post.dto.response.ResponsePostListDto;
import com.mailpug.homework.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * [게시글 등록]
     *
     * 입력받은 게시글( 카테고리, 제목, 내용), 사용자 아이디로 게시글 등록하는 서비스
     *
     * @param postDto CreatePostDto
     * @param userId String
     * @return Long 게시글 번호
     */
    @Transactional
    public Long addPost(CreatePostDto postDto, String userId) {

        Post post = Post.builder()
                .name(postDto.getName())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(userId)
                .build();

        postRepository.save(post);

        return post.getId();
    }

    /**
     * [게시글 단건 조회]
     *
     * 게시글 번호로 단일 게시글 조회하는 서비스
     *
     * @param postId Long 게시글 번호
     * @return ResponsePostDto 조회된 게시글 정보
     */
    public ResponsePostDto getPost(Long postId) {

        Post post = findPost(postId);

        return ResponsePostDto.builder()
                .id(post.getId())
                .name(post.getName())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getName())
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .build();

    }

    /**
     * [게시글 수정]
     *
     * 게시글 번호와 수정할 내용을 받아서 게시글 수정하는 서비스
     *
     * @param updatePostDto UpdatePostDto 수정할 게시글 정보
     * @param userId String 사용자 아이디
     * @return Long 수정된 게시글의 ID
     */
    @Transactional
    public Long modifyPost(UpdatePostDto updatePostDto, String userId) {

        Post post = findPost(updatePostDto.getId());

        validatedUserId(post.getAuthor(), userId);

        post.updatePost(updatePostDto);

        postRepository.save(post);

        return  post.getId();
    }

    /**
     * [게시글 삭제]
     *
     * 게시글 번호로 게시글 삭제하는 서비스
     *
     * @param postId Long 게시글 번호
     * @param userId String 사용자 아이디
     */
    @Transactional
    public void deletePost(Long postId, String userId) {

        Post post = findPost(postId);

        validatedUserId(post.getAuthor(), userId);

        postRepository.delete(post);
    }

    /**
     * [게시글 목록 조회]
     *
     * 페이지 정보와 게시글 카테고리를 받아서 게시글 목록 조회하는 서비스
     *
     * @param pageRequestDto PageRequestDto 페이지 정보와 검색어
     * @return PageResultDto<ResponsePostListDto> 게시글 목록과 페이징 정보
     * @throws BusinessExceptionHandler 조회된 데이터가 없을 경우
     */
    public PageResultDto<ResponsePostListDto> getPostList(PageRequestDto pageRequestDto) {
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable(Sort.by("id"));

        Page<ResponsePostListDto> result = postRepository.getPostList(keyword, pageable);

        if (result.getContent().isEmpty()) throwNoMatchesFound();


        return new PageResultDto<>(result);
    }


    /**
     * [유저 권한 확인]
     *
     * 수정 및 삭제하는 게시글의 작성자와 사용자 아이디가 같은지 비교하여 권한 확인
     *
     * @param postUserId String 게시글 작성자 아이디
     * @param userId String 사용자 아이디
     * @throws BusinessExceptionHandler 권한이 없을 경우 예외 발생 (ErrorCode.FORBIDDEN_ERROR)
     */
    private void validatedUserId(String postUserId, String userId) {

        if (!postUserId.equals(userId)) {
            throw new BusinessExceptionHandler("권한이 없습니다.", ErrorCode.FORBIDDEN_ERROR);
        }

    }

    /**
     * [게시글 찾기]
     *
     * 입력받은 게시글 번호의 게시글이 있는지 확인하고 찾은 게시글 반환
     *
     * @param postId Long 게시글 번호
     * @return Post 조회된 게시글 객체
     * @throws BusinessExceptionHandler 조회된 데이터가 없을 경우 예외 발생
     */
    private Post findPost(Long postId) {

        Optional<Post> result = postRepository.findById(postId);

        if (result.isEmpty()) throwNoMatchesFound();

        return result.get();
    }


    /**
     * BusinessExceptionHandler(ErrorCode.NO_MATCHES_FOUND) 예외를 던지는 메서드
     *
     * @throws BusinessExceptionHandler
     */
    private void throwNoMatchesFound() {
        throw new BusinessExceptionHandler(ErrorCode.NO_MATCHES_FOUND);
    }

}
