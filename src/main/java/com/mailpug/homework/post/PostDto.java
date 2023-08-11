package com.mailpug.homework.post;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
public class PostDto {

    private Long id;

    private String category;

    @NotBlank(message = "게시글 제목이 빈 값 입니다.")
    @Length(min = 1, max = 100, message = "게시글 제목은 100자를 초과할 수 없습니다.")
    private String title;

    private String content;

    private String author;

    @Builder
    public PostDto(Long id, String category, String title, String content, String author) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
