package com.mtk.loginauthapi.infra.security;

import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;

public class Constants
{
//	@Value("${api.security.token.secret}")
	private static final String SECRET_KEY = "my-secret-key";
	public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
	public static final Instant EXPIRATION_TIME = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-4"));
	public static final String ISSUER = "login-auth-api";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String ROLE_USER = "ROLE_USER";
}
