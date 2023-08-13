package com.mailpug.homework.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "게시글 응답 DTO")
@Getter
@NoArgsConstructor
public class ResponsePostDto {

    @Schema(description = "게시글 번호")
    private Long id;

    @Schema(description = "게시글 카테고리")
    private String name;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "게시글 작성자")
    private String author;

    @Schema(description = "게시글 작성 일자")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createAt;

    @Schema(description = "게시글 수정 일자")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updateAt;

    @Builder
    public ResponsePostDto(Long id, String name, String title, String content, String author, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }


}
