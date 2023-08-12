package com.mailpug.homework.post.service;

import com.mailpug.homework.common.codes.ErrorCode;
import com.mailpug.homework.config.exception.BusinessExceptionHandler;
import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;
import com.mailpug.homework.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
                .build();
    }

    public Long modifyPost(PostDto postDto, String userId) {

        Post post = findPost(postDto.getId());

        validatedUserId(post.getAuthor(), userId);

        post.updatePost(postDto);

        postRepository.save(post);

        return  post.getId();
    }

    public void deletePost(Long postId, String userId) {

        Post post = findPost(postId);

        validatedUserId(post.getAuthor(), userId);

        postRepository.delete(post);
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
