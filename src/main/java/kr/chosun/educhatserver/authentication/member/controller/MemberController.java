package kr.chosun.educhatserver.authentication.member.controller;

import kr.chosun.educhatserver.authentication.member.domain.Member;
import kr.chosun.educhatserver.authentication.member.domain.MemberDTO;
import kr.chosun.educhatserver.authentication.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public Map<String, String> signup(@RequestBody MemberDTO memberDTO) {
		log.info("member controller");
		log.info("memberDTO = {}", memberDTO);

		Map<String, String> response = new HashMap<>();
		Optional<Member> member = memberService.findByEmail(memberDTO.getEmail());

		if(member.isPresent()) {
			response.put("error", "이미 존재하는 이메일입니다");
		} else {
			memberService.saveMember(memberDTO);
			response.put("success", "성공적으로 처리하였습니다");
		}

		return response;
	}
}
