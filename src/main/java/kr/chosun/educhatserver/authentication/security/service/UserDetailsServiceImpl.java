package kr.chosun.educhatserver.authentication.security.service;

import kr.chosun.educhatserver.authentication.member.domain.PrincipalDetail;
import kr.chosun.educhatserver.authentication.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return memberRepository.findByEmail(username)
				.map(member -> new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString()))))
				.orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 사용자입니다"));
	}
}
