package com.leets.commitatobe.global.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPage<T> {
    private List<T> content; // 응답 DTO 내용!
    private boolean isFirst; // 첫번째 페이지인지 여부
    private boolean isLast; // 마지막 페이지인지 여부
    private int totalPage; // 전체 페이지 개수
    private long totalElements; // 전체 응답리스트의 요소 개수
    private int size; // 페이지 한번에 담아 보내는 요소의 크기
    private int currPage; // 현재 페이지 번호
    private boolean hasNext; // 다음페이지가 남아 있는지 여부

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.totalPage = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.currPage = page.getNumber();
        this.hasNext = page.hasNext();
    }

    @JsonProperty("content")
    public List<T> getContent() {
        return content;
    }

    @JsonProperty("isFirst")
    public boolean isFirst() {
        return isFirst;
    }

    @JsonProperty("isLast")
    public boolean isLast() {
        return isLast;
    }

    @JsonProperty("totalPage")
    public int getTotalPage() {
        return totalPage;
    }

    @JsonProperty("totalElements")
    public long getTotalElements() {
        return totalElements;
    }

    @JsonProperty("size")
    public int getSize() {
        return size;
    }

    @JsonProperty("currPage")
    public int getCurrPage() {
        return currPage;
    }

    @JsonProperty("hasNext")
    public boolean isHasNext() {
        return hasNext;
    }
}
