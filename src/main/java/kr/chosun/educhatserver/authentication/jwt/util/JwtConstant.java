package kr.chosun.educhatserver.authentication.jwt.util;

public class JwtConstant {

	public static final String key = "DG3K2NG9lK3T2FLfnO283HO1NFLAy9FGJ23UM9Rv923YRV923HT";
	public static final long ACCESS_EXP_TIME = 10;
	public static final long REFRESH_EXP_TIME = 60 * 24;

	public static final String JWT_HEADER = "Authorization";
	public static final String JWT_TYPE = "Bearer ";
}
