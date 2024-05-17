package kr.chosun.educhatserver.authentication.member.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberDTO {
	private String email;
	private String password;
	private String name;
}
