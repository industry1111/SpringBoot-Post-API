package com.mailpug.homework.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * [공통] API 통신시 페이징 데이터 및 DTO LIST 반환
 */
@Getter
public class PageResultDto<DTO> {

    private List<DTO> dtoList;

    private int totalPage;

    private int page;

    private int size;

    private int start, end;

    public PageResultDto(Page<DTO> result) {
        this.dtoList = result.getContent();
        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() +1;
        this.size = pageable.getPageSize();

        int tempEnd = (int) (Math.ceil((page/10.0)))* 10;
        start = tempEnd - 9;

        end = totalPage > tempEnd ? tempEnd : totalPage;

    }
}
