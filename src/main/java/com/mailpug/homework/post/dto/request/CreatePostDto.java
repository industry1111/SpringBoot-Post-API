package com.mailpug.homework.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Schema(description = "게시글 생성 DTO")
@Getter
@NoArgsConstructor
public class CreatePostDto {

    @Schema(description = "게시글 카테고리",nullable = false)
    @NotBlank(message = "게시글의 카테고리는 필수 값 입니다.")
    private String name;

    @Schema(description = "게시글 제목",nullable = false, maxLength = 100)
    @NotBlank(message = "게시글의 제목은 필수 값 입니다.")
    @Length(min = 1, max = 100, message = "게시글 제목은 1~100자 사이로 작성해 주세요.")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Builder
    public CreatePostDto(String name, String title, String content) {
        this.name = name;
        this.title = title;
        this.content = content;
    }
}
