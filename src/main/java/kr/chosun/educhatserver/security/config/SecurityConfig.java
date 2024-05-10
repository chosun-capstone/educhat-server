package kr.chosun.educhatserver.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig {

	private static final String[] WHITELIST = {};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		//CSRF
		http.csrf(AbstractHttpConfigurer::disable);

		//CORS
		http.cors(Customizer.withDefaults());

		//FormLogin, BasicHttp disable
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);

		//JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가

		//권한 규칙
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(WHITELIST).permitAll()
				.anyRequest().authenticated());

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
}
