package kr.chosun.educhatserver.security.controller;

import jakarta.validation.Valid;
import kr.chosun.educhatserver.security.dto.LoginDto;
import kr.chosun.educhatserver.security.dto.UserDto;
import kr.chosun.educhatserver.security.entity.User;
import kr.chosun.educhatserver.security.service.CustomOAuth2UserService;
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

	private final CustomOAuth2UserService customOAuth2UserService;

	@PostMapping("/signup")
	public ResponseEntity<User> signup(
			@Valid @RequestBody UserDto userDto
	) {
		return ResponseEntity.ok(customOAuth2UserService.signup(userDto));
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(
			@Valid @RequestBody LoginDto loginDto
	) {
		return ResponseEntity.ok(customOAuth2UserService.login(loginDto));
	}

}
