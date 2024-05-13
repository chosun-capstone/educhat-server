package kr.chosun.educhatserver.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

	@NotNull(message = "아이디 입력은 필수")
	private String username;

	@NotNull(message = "패스워드 입력은 필수")
	private String password;
}

