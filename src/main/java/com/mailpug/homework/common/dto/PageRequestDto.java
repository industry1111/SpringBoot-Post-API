package com.mailpug.homework.common.dto;

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

    private int keyword;

    public PageRequestDto() {
        this.page = 1;
        this.size = 5;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page-1,size,sort);
    }
}
