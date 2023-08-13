package com.mailpug.homework.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * [공통] API 통신시 페이징 입력 및 keyword 관리
 */
@Getter
public class PageRequestDto {

    private int page;

    private int size;

    private String keyword;

    public PageRequestDto() {
        this.page =1;
        this.size = 5;
    }

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
