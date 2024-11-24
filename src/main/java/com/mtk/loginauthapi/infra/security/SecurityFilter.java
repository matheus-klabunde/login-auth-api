package com.mtk.loginauthapi.infra.security;

import static com.mtk.loginauthapi.infra.security.Constants.*;

import com.mtk.loginauthapi.domain.model.User;
import com.mtk.loginauthapi.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityFilter extends OncePerRequestFilter
{
	private final TokenService tokenService;
	private final UserRepository userRepository;

	@Autowired
	public SecurityFilter(TokenService tokenService, UserRepository userRepository)
	{
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException
	{
		String token = this.recoverToken(request);
		String login = tokenService.validateToken(token);

		if (login != null)
		{
			User user = userRepository.findByEmail(login)
				.orElseThrow(() -> new RuntimeException("User not found"));
			var authorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER));
			var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	private String recoverToken(HttpServletRequest request)
	{
		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		if (authHeader == null)
		{
			return null;
		}
		return authHeader.replace(TOKEN_PREFIX, "");
	}
}
