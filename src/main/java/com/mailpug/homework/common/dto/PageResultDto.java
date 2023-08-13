package com.mailpug.homework.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * [공통] API 통신시 페이징 데이터 및 DTO LIST 반환
 */
@Schema(description = "API 통신시 페이징 데이터 및 DTO LIST 반환")
@Getter
public class PageResultDto<DTO> {

    @Schema(description = "결과 목록")
    private List<DTO> dtoList;

    @Schema(description = "전체 페이지 수")
    private int totalPage;

    @Schema(description = "현재 페이지")
    private int page;

    @Schema(description = "페이지당 보여줄 수")
    private int size;


    public PageResultDto(Page<DTO> result) {
        this.dtoList = result.getContent();
        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() +1;
        this.size = pageable.getPageSize();
    }
}
