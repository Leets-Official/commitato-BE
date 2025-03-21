package com.leets.commitatobe.global.response.code.status;

import org.springframework.http.HttpStatus;

import com.leets.commitatobe.global.response.code.BaseCode;
import com.leets.commitatobe.global.response.code.dto.ReasonDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

	// 일반적인 응답
	_OK(HttpStatus.OK, "COMMON200", "성공입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	@Override
	public ReasonDto getReason() {
		return ReasonDto.builder()
			.isSuccess(true)
			.code(code)
			.message(message)
			.build();
	}

	@Override
	public ReasonDto getReasonHttpStatus() {
		return ReasonDto.builder()
			.httpStatus(httpStatus)
			.isSuccess(true)
			.code(code)
			.message(message)
			.build();
	}
}
