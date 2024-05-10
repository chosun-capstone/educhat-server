package kr.chosun.educhatserver.security.controller;

import jakarta.validation.Valid;
import kr.chosun.educhatserver.security.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

	@PostMapping("/login")
	public ResponseEntity<String> getUserProfile(
			@Valid @RequestBody LoginRequestDto requestDto
	) {

	}
}
