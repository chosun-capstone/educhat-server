package kr.chosun.educhatserver.security.service;

import kr.chosun.educhatserver.security.dto.LoginRequestDto;
import kr.chosun.educhatserver.security.dto.UserDto;
import kr.chosun.educhatserver.security.entity.User;
import kr.chosun.educhatserver.security.repository.UserRepository;
import kr.chosun.educhatserver.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Transactional
	public String login(LoginRequestDto requestDto) {
		String email = requestDto.getEmail();
		String password = requestDto.getPassword();
		User user = userRepository.findUserByEmail(email);

		if(user == null) {
			throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
		}

		if(!encoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		UserDto userDto = UserDto.toEntity(user);

		return jwtUtil.createAccessToken(userDto);
	}
}
