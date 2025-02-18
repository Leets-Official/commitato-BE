package com.leets.commitatobe.global.response.code;

import com.leets.commitatobe.global.response.code.dto.ErrorReasonDto;

public interface BaseErrorCode {

	public ErrorReasonDto getReason();

	public ErrorReasonDto getReasonHttpStatus();
}
