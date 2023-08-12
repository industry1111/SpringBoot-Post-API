package com.mailpug.homework.post.repository;

import com.mailpug.homework.config.Appconfig;
import com.mailpug.homework.post.Post;
import com.mailpug.homework.post.PostDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Import(Appconfig.class)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Nested
    @DisplayName("게시글 찾기")
    class findPostTest {

        @DisplayName("성공")
        @Test
        void success() {
            //given
            Post post = Post.builder()
                    .category("SpringBoot")
                    .title("게시글 생성")
                    .content("게시글내용")
                    .author("user1")
                    .build();

            postRepository.save(post);

            //when
            Post findPost = postRepository.findById(post.getId()).get();

            //then
            assertThat(findPost.getId()).isEqualTo(post.getId());
        }

        @DisplayName("결과 없음")
        @Test
        void isEmpty() {
            //given
            //when
            boolean result = postRepository.existsById(99L);

            //then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회")
    class getPosts {

        @DisplayName("성공")
        @Test
        void success() {
            //given
            for (int i = 0; i < 20; i++) {
                Post post = Post.builder()
                        .category("카테고리" +i/5)
                        .title("게시글 생성"+i)
                        .content("게시글내용"+i)
                        .author("user")
                        .build();

                postRepository.save(post);
            }

            String keyword = "카테고리0";
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);

            //when
            List<PostDto> result = postRepository.getPosts(keyword, pageable);

            //then
            assertThat(result.size()).isNotEqualTo(size);
            assertThat(result.size()).isEqualTo(5);
        }

        @DisplayName("성공 - 카테고리 미입력")
        @Test
        void successCategoryIsEmpty() {
            //given
            for (int i = 0; i < 20; i++) {
                Post post = Post.builder()
                        .category("카테고리" +i/5)
                        .title("게시글 생성"+i)
                        .content("게시글내용"+i)
                        .author("user")
                        .build();

                postRepository.save(post);
            }

            String keyword = "";
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);

            //when
            List<PostDto> result = postRepository.getPosts(keyword, pageable);

            //then
            assertThat(result.size()).isEqualTo(size);
        }

        @DisplayName("실패 - 등록된 게시글이 존재 하지 않음")
        @Test
        void isEmpty() {
            //given
            String keyword = "카테고리0";
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);

            //when
            List<PostDto> result = postRepository.getPosts(keyword, pageable);

            //then
            assertThat(result).isEmpty();
        }

        @DisplayName("실패 - 해당 카테고리에는 게시글이 없음")
        @Test
        void invalidCategory() {
            //given
            for (int i = 0; i < 20; i++) {
                Post post = Post.builder()
                        .category("카테고리" +i/5)
                        .title("게시글 생성"+i)
                        .content("게시글내용"+i)
                        .author("user")
                        .build();

                postRepository.save(post);
            }

            //when
            String keyword = "카테고리99";
            int page = 0;
            int size = 10;
            Pageable pageable = PageRequest.of(page, size);
            List<PostDto> result = postRepository.getPosts(keyword, pageable);

            //then
            assertThat(result).isEmpty();
        }
    }
}