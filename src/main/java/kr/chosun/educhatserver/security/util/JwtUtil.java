package kr.chosun.educhatserver.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.chosun.educhatserver.security.dto.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

	private final Key key;
	private final long accessTokenExpTime;

	public JwtUtil(
			@Value("${jwt.secret}") String secretKey,
			@Value("${jwt.expiration_time}") long accessTokenExpTime
	) {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		this.accessTokenExpTime = accessTokenExpTime;
	}

	//access token generate
	public String createAccessToken(UserRequestDto user) {

		return createToken(user, accessTokenExpTime);
	}

	//jwt generate
	public String createToken(UserRequestDto user, long expireTime) {

		Claims claims = Jwts.claims();
		claims.put("userId", user.getUserId());
		claims.put("email", user.getEmail());
		claims.put("role", user.getRole());

		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(Date.from(now.toInstant()))
				.setExpiration(Date.from(tokenValidity.toInstant()))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}

	//user id find
	public Long getUserId(String token) {

		return parseClaims(token).get("UserId", long.class);
	}

	//jwt valid
	public boolean validateToken(String token) {

		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty", e);
		}

		return false;
	}

	//jwt claims
	public Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

}
