package kr.chosun.educhatserver.security.dto;

import kr.chosun.educhatserver.security.constant.Role;
import lombok.Data;

@Data
public class UserDto {

	private Long userId;

	private String email;

	private String name;

	private String password;

	private Role role;
}
