package kr.chosun.educhatserver.security.service;

import kr.chosun.educhatserver.security.dto.LoginDto;
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
	public User signup(UserDto userDto) {

		if(userRepository.existsByEmail(userDto.getEmail())) {
			throw new RuntimeException("이미 가입되어 있는 유저입니다");
		}

		User user = User.builder()
				.userId(userDto.getUserId())
				.email(userDto.getEmail())
				.name(userDto.getName())
				.password(userDto.getPassword())
				.role(userDto.getRole())
				.build();

		return userRepository.save(user);
	}

	@Transactional
	public User login(LoginDto loginDto) {

		String username = loginDto.getUsername();
		String password = loginDto.getPassword();
		User user = userRepository.findUserByEmail(username);

		if(user == null) {
			throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
		}

		if(!encoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		UserDto userDto = UserDto.toUser(user);

		return jwtUtil.createAccessToken(userDto);
	}

}
