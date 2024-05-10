package kr.chosun.educhatserver.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

	@NotNull(message = "이메일 입력은 필수")
	@Email
	private String email;

	@NotNull(message = "패스워드 입력은 필수")
	private String password;
}
