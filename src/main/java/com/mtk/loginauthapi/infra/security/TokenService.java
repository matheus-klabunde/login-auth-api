package com.mtk.loginauthapi.infra.security;

import static com.mtk.loginauthapi.infra.security.Constants.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mtk.loginauthapi.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class TokenService
{
	public String generateToken(User user)
	{
		try
		{
			return JWT.create().withIssuer("login-auth-api").withSubject(user.getEmail())
				.withExpiresAt(EXPIRATION_TIME).sign(ALGORITHM);
		}
		catch (JWTCreationException e)
		{
			throw new RuntimeException("Error while authentication", e);
		}
	}

	public String validateToken(String token)
	{
		try
		{
			return JWT.require(ALGORITHM).withIssuer(ISSUER).build().verify(token).getSubject();
		}
		catch (JWTVerificationException e)
		{
			return null;
		}
	}
}
