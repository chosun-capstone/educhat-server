package kr.chosun.educhatserver.authentication.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {

	private static final String[] WHITELIST = {"/files/**"};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
				.requestMatchers("/error", "/favicon.ico");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		//CSRF
		http.csrf(AbstractHttpConfigurer::disable);

		//CORS
		http.cors(httpSecurityCorsConfigurer ->
				httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

		//FormLogin, BasicHttp disable
		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);
		http.logout(AbstractHttpConfigurer::disable);

		http.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.addAllowedHeader("*");
		configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "HEAD"));
		configuration.setAllowCredentials(true);
		configuration.addAllowedOrigin("http://localhost:8080");
		configuration.addAllowedOrigin("https://educhat.yeongmin.kr");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
