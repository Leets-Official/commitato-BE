package com.leets.commitatobe.global.response.code;

import com.leets.commitatobe.global.response.code.dto.ReasonDto;

public interface BaseCode {

	public ReasonDto getReason();

	public ReasonDto getReasonHttpStatus();
}
