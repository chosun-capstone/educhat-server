package kr.chosun.educhatserver.authentication.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	USER("USER"), ADMIN("ADMIN");
	private final String value;
}
