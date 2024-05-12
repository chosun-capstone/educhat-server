package kr.chosun.educhatserver.security.dto;

import kr.chosun.educhatserver.security.constant.Role;
import kr.chosun.educhatserver.security.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {

	private Long userId;

	private String email;

	private String name;

	private String password;

	private Role role;

	public static UserRequestDto toUser(User user) {

		return UserRequestDto.builder()
				.userId(user.getUserId())
				.email(user.getEmail())
				.name(user.getName())
				.password(user.getPassword())
				.role(user.getRole())
				.build();
	}
}
