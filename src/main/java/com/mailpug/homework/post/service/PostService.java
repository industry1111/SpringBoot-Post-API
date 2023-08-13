package com.mailpug.homework.post.service;

import com.mailpug.homework.common.codes.ErrorCode;
import com.mailpug.homework.common.dto.PageRequestDto;
import com.mailpug.homework.common.dto.PageResultDto;
import com.mailpug.homework.config.exception.BusinessExceptionHandler;
import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;
import com.mailpug.homework.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long addPost(PostDto postDto, String userId) {

        Post post = Post.builder()
                .category(postDto.getCategory())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(userId)
                .build();

        postRepository.save(post);

        return post.getId();
    }

    public PostDto getPost(Long postId) {

        Post post = findPost(postId);

        return PostDto.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getCategory())
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .build();

    }

    @Transactional
    public Long modifyPost(PostDto postDto, String userId) {

        Post post = findPost(postDto.getId());

        validatedUserId(post.getAuthor(), userId);

        post.updatePost(postDto);

        postRepository.save(post);

        return  post.getId();
    }

    @Transactional
    public void deletePost(Long postId, String userId) {

        Post post = findPost(postId);

        validatedUserId(post.getAuthor(), userId);

        postRepository.delete(post);
    }

    public PageResultDto<PostDto> getPostList(PageRequestDto pageRequestDto) {
        String keyword = pageRequestDto.getKeyword();
        Pageable pageable = pageRequestDto.getPageable(Sort.by("id"));

        Page<PostDto> result = postRepository.getPostList(keyword, pageable);

        return new PageResultDto<>(result);
    }


    private void validatedUserId(String postUserId, String userId) {

        if (!postUserId.equals(userId)) {
            throw new BusinessExceptionHandler("권한이 없습니다.", ErrorCode.FORBIDDEN_ERROR);
        }

    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessExceptionHandler("존재하지 않는 게시글 번호 입니다.",ErrorCode.NULL_POINT_ERROR));
    }
}
