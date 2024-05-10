package kr.chosun.educhatserver.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;

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
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.accessTokenExpTime = accessTokenExpTime;
	}

	//access token generate
	public String createAccessToken() {
		return null;
	}

	//jwt generate
	public String createToken() {
		return null;
	}

	//user id find
	public Long getUserId(String token) {
		return null;
	}

	//jwt valid
	public boolean validateToken(String token) {
		return false;
	}

	//jwt claims
	public Claims parseClaims(String accessToken) {
		return null;
	}

}
