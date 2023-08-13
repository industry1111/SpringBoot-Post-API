package com.mailpug.homework.post.dto.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "게시글 목록 응답 DTO")
@Getter
@NoArgsConstructor
public class ResponsePostListDto {

    @Schema(description = "게시글 번호")
    private Long id;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 작성자")
    private String author;

    @Schema(description = "게시글 작성 일자")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createAt;

    @Schema(description = "게시글 수정 일자")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updateAt;

    @Builder
    @QueryProjection
    public ResponsePostListDto(Long id, String title, String author, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}