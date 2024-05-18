package kr.chosun.educhatserver.authentication.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kr.chosun.educhatserver.authentication.jwt.exception.CustomExpiredJwtException;
import kr.chosun.educhatserver.authentication.jwt.exception.CustomJwtException;
import kr.chosun.educhatserver.authentication.member.domain.Member;
import kr.chosun.educhatserver.authentication.member.domain.PrincipalDetail;
import kr.chosun.educhatserver.authentication.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtUtil {

	@Value("${jwt.key}")
	private String key;
	private static SecretKey secretKey;
	private final TokenService tokenService;
	private static final String KEY_ROLE = "role";

	@PostConstruct
	private void setSecretKey() {
		secretKey = Keys.hmacShaKeyFor(key.getBytes());
	}

	public String generateAccessToken(Authentication authentication) {
		return generateToken(authentication, JwtConstant.ACCESS_EXP_TIME);
	}

	public String generateToken(Authentication authentication, long expireTime) {
		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + expireTime);

		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining());

		return Jwts.builder()
				.setSubject(authentication.getName())
				.claim(JwtConstant.JWT_TYPE, authorities)
				.setIssuedAt(expiredDate)
				.setExpiration(expiredDate)
				.signWith(secretKey)
				.compact();
	}

	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

		String email = (String) claims.get("email");
		String name = (String) claims.get("name");
		String role = (String) claims.get("role");
		Role memberRole = Role.valueOf(role);

		Member member = Member.builder().email(email).name(name).role(memberRole).build();
		PrincipalDetail principalDetail = new PrincipalDetail(member, authorities);

		return new UsernamePasswordAuthenticationToken(principalDetail, "", authorities);
	}

	private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
		return Collections.singletonList(new SimpleGrantedAuthority(
				claims.get(KEY_ROLE).toString()));
	}

	private static Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw new CustomExpiredJwtException("토큰이 만료되었습니다", e);
		} catch (Exception e) {
			throw new CustomJwtException("에러");
		}
	}

	public static Map<String, Object> validateToken(String token) {
		Map<String, Object> claim = null;
		try {
			claim = parseClaims(token);
		} catch(ExpiredJwtException expiredJwtException){
			throw new CustomExpiredJwtException("토큰이 만료되었습니다", expiredJwtException);
		} catch(Exception e){
			throw new CustomJwtException("Error");
		}
		return claim;
	}

	public static boolean isExpired(String token) {
		try {
			validateToken(token);
		} catch (Exception e) {
			return (e instanceof CustomExpiredJwtException);
		}
		return false;
	}

	public static long tokenRemainTime(Integer expTime) {
		Date expDate = new Date((long) expTime * (1000));
		long remainMs = expDate.getTime() - System.currentTimeMillis();
		return remainMs / (1000 * 60);
	}
}
