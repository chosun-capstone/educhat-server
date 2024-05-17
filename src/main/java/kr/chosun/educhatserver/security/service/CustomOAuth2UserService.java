package kr.chosun.educhatserver.security.service;

import kr.chosun.educhatserver.security.dto.OAuth2UserInfo;
import kr.chosun.educhatserver.security.dto.PrincipalDetails;
import kr.chosun.educhatserver.security.dto.UserDto;
import kr.chosun.educhatserver.security.entity.User;
import kr.chosun.educhatserver.security.repository.UserRepository;
import kr.chosun.educhatserver.security.util.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();

		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

		User user = getOrSave(oAuth2UserInfo);

		return new PrincipalDetails(user, oAuth2UserAttributes, userNameAttributeName);
	}

	private User getOrSave(OAuth2UserInfo oAuth2UserInfo) {
		User user = userRepository.findUserByEmail(oAuth2UserInfo.email())
				.(oAuth2UserInfo::toEntity);
		return userRepository.save(user);
	}
}
