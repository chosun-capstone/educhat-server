package kr.chosun.educhatserver.authentication.oauth2.service;

import kr.chosun.educhatserver.authentication.member.domain.Member;
import kr.chosun.educhatserver.authentication.member.domain.PrincipalDetail;
import kr.chosun.educhatserver.authentication.member.domain.Role;
import kr.chosun.educhatserver.authentication.member.repository.MemberRepository;
import kr.chosun.educhatserver.authentication.oauth2.user.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> attributes = oAuth2User.getAttributes();

		String usernameAttributeName = userRequest.getClientRegistration()
				.getProviderDetails()
				.getUserInfoEndpoint()
				.getUserNameAttributeName();

		KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
		String socialId = kakaoUserInfo.getSocialId();
		String name = kakaoUserInfo.getName();

		Optional<Member> bySocialId = memberRepository.findBySocialId(socialId);
		Member member = bySocialId.orElseGet(() -> saveSocialMember(socialId, name));

		return new PrincipalDetail(member, Collections.singleton(
				new SimpleGrantedAuthority(member.getRole().getValue())), attributes);
	}

	public Member saveSocialMember(String socialId, String name) {

		Member newMember = Member.builder().socialId(socialId).name(name).role(Role.USER).build();
		return memberRepository.save(newMember);
	}
}
