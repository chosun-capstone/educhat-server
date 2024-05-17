package kr.chosun.educhatserver.security.dto;

import jakarta.security.auth.message.AuthException;
import kr.chosun.educhatserver.security.constant.Role;
import kr.chosun.educhatserver.security.entity.User;
import lombok.Builder;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.crypto.KeyGenerator;
import java.util.Map;

@Builder
public record OAuth2UserInfo(
		String name,
		String email,
		String profile
) {

	public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
		return switch (registrationId) {
			case "google" -> ofGoogle(attributes);
			case "kakao" -> ofKakao(attributes);
			default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
		}
	}

	private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
		return OAuth2UserInfo.builder()
				.name((String) attributes.get("name"))
				.email((String) attributes.get("email"))
				.profile((String) attributes.get("picture"))
				.build();
	}

	private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>) account.get("profile");

		return OAuth2UserInfo.builder()
				.name((String) profile.get("nickname"))
				.email((String) account.get("email"))
				.profile((String) profile.get("profile_image_url"))
				.build();
	}

	public User toEntity() {
		return User.builder()
				.name(name)
				.email(email)
				.profile(profile)
				.userKey(KeyGenerators.string().generateKey())
				.role(Role.USER)
				.build();
	}
}
