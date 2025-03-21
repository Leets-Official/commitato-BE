package com.leets.commitatobe.global.response.code.status;

import org.springframework.http.HttpStatus;

import com.leets.commitatobe.global.response.code.BaseErrorCode;
import com.leets.commitatobe.global.response.code.dto.ErrorReasonDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

	// 일반 응답
	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
	_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

	// JWT 관련
	_JWT_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT_001", "Header에 JWT가 존재하지 않습니다."),
	_GITHUB_TOKEN_GENERATION_ERROR(HttpStatus.NOT_FOUND, "JWT_002", "깃허브에서 엑세스 토큰을 발급하는 과정에서 오류가 발생했습니다."),
	_GITHUB_JSON_PARSING_ERROR(HttpStatus.NOT_FOUND, "JWT_003", "엑세스 토큰을 JSON에서 가져오는 과정에 오류가 발생했습니다."),
	_REFRESH_TOKEN_EXPIRED(HttpStatus.NOT_FOUND, "JWT_004", "리프레시 토큰이 만료되어 재로그인이 필요합니다."),

	// 리다이렉션
	_REDIRECT_ERROR(HttpStatus.NOT_FOUND, "REDIRECT_001", "리다이렉트 과정에서 오류가 발생했습니다."),

	// S3 관련
	_S3_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_001", "S3에 존재하지 않는 이미지입니다."),

	// 커밋 관련
	_COMMIT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMIT_001", "커밋이 없습니다."),

	// GitHub API 관련
	_GIT_URL_INCORRECT(HttpStatus.BAD_REQUEST, "GIT_001", "잘못된 URL로 요청하고 있습니다."),

	// 인코딩 오류
	_ENCRYPT_ERROR(HttpStatus.BAD_REQUEST, "ENCRYPT_001", "토큰 인코딩 과정에서 오류가 발생했습니다."),

	// 디코딩 오류
	_DECRYPT_ERROR(HttpStatus.BAD_REQUEST, "DECRYPT_001", "토큰 디코딩 과정에서 오류가 발생했습니다."),

	// 검색 기능 관련
	_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "해당 유저가 존재하지 않습니다"),

	// 티어 관련
	_TIER_NOT_FOUND(HttpStatus.NOT_FOUND, "TIER_001", "해당 경험치에 맞는 티어를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	@Override
	public ErrorReasonDto getReason() {
		return ErrorReasonDto.builder()
			.isSuccess(false)
			.code(code)
			.message(message)
			.build();
	}

	@Override
	public ErrorReasonDto getReasonHttpStatus() {
		return ErrorReasonDto.builder()
			.httpStatus(httpStatus)
			.isSuccess(false)
			.code(code)
			.message(message)
			.build();
	}
}
