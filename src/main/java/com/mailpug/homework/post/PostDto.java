package com.mailpug.homework.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostDto {

    private Long id;

    private String category;

    @Length(min = 1, max = 100, message = "게시글 제목은 1~100자 사이로 작성해 주세요.")
    private String title;

    private String content;

    private String author;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    @QueryProjection
    public PostDto(Long id, String category, String title, String content, String author, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }


}
