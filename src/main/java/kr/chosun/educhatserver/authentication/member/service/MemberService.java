package kr.chosun.educhatserver.authentication.member.service;

import kr.chosun.educhatserver.authentication.member.domain.Member;
import kr.chosun.educhatserver.authentication.member.domain.MemberDTO;
import kr.chosun.educhatserver.authentication.member.domain.Role;
import kr.chosun.educhatserver.authentication.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	public Member saveMember(MemberDTO memberDTO) {
		Member member = Member.builder()
				.name(memberDTO.getName())
				.email(memberDTO.getEmail())
				.password(passwordEncoder.encode(memberDTO.getPassword()))
				.role(Role.USER).build();
		return memberRepository.save(member);
	}
}
