package com.mailpug.homework.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * [공통] API 통신시 페이징 입력 및 keyword 관리
 */
@Schema(description = "페이징 처리를 위한 DTO")
@Getter
public class PageRequestDto {

    @Schema(description = "게시글 목록의 보여줄 페이지",defaultValue = "1")
    private int page;

    @Schema(description = "보여줄 게시글 목록 수",defaultValue = "5")
    private int size;

    @Schema(description = "게시글 카테고리")
    private String keyword;

    @Builder
    public PageRequestDto(int page, int size, String keyword) {
        this.page = page;
        this.size = size;
        this.keyword = keyword;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page-1,size,sort);
    }
}
