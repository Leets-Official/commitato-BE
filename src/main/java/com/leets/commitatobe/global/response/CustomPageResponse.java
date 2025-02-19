package com.leets.commitatobe.global.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomPageResponse<T>(
	@JsonProperty("content") List<T> content,
	@JsonProperty("isFirst") boolean isFirst,
	@JsonProperty("isLast") boolean isLast,
	@JsonProperty("totalPage") int totalPage,
	@JsonProperty("totalElements") long totalElements,
	@JsonProperty("size") int size,
	@JsonProperty("currPage") int currPage,
	@JsonProperty("hasNext") boolean hasNext
) {
	public static <T> CustomPageResponse<T> from(Page<T> page) {
		return new CustomPageResponse<>(
			page.getContent(),
			page.isFirst(),
			page.isLast(),
			page.getTotalPages(),
			page.getTotalElements(),
			page.getSize(),
			page.getNumber(),
			page.hasNext()
		);
	}
}
