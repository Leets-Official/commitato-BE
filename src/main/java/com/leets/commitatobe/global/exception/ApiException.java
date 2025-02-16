package com.leets.commitatobe.global.exception;

import com.leets.commitatobe.global.response.code.ErrorReasonDto;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;

public class ApiException extends RuntimeException {

	private final ErrorStatus errorStatus;

	public ApiException(ErrorStatus errorStatus) {
		super(errorStatus.getMessage());
		this.errorStatus = errorStatus;
	}

	public ErrorReasonDto getErrorReason() {
		return this.errorStatus.getReason();
	}

	public ErrorReasonDto getErrorReasonHttpStatus() {
		return this.errorStatus.getReasonHttpStatus();
	}
}
