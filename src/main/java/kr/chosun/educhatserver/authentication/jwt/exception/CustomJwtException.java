package kr.chosun.educhatserver.authentication.jwt.exception;

public class CustomJwtException extends RuntimeException {

	public CustomJwtException(String message) {
		super(message);
	}
}
