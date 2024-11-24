package com.mtk.loginauthapi.application.services;

import com.mtk.loginauthapi.domain.model.User;
import com.mtk.loginauthapi.infra.security.TokenService;
import com.mtk.loginauthapi.presentation.dto.AuthResponseDTO;
import com.mtk.loginauthapi.presentation.dto.LoginRequestDTO;
import com.mtk.loginauthapi.presentation.dto.RegisterRequestDTO;
import com.mtk.loginauthapi.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	@Autowired
	public AuthService(UserRepository repository, PasswordEncoder passwordEncoder,
		TokenService tokenService)
	{
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	public AuthResponseDTO login(LoginRequestDTO loginRequest)
	{
		User user = this.repository.findByEmail(loginRequest.email())
			.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(loginRequest.password(), user.getPassword()))
		{
			throw new RuntimeException("Wrong password");
		}

		String token = tokenService.generateToken(user);
		return new AuthResponseDTO(user.getName(), token);
	}

	public AuthResponseDTO register(RegisterRequestDTO registerRequest)
	{
		Optional<User> user = this.repository.findByEmail(registerRequest.email());

		if (user.isPresent())
		{
			throw new RuntimeException("Email already in use");
		}

		User newUser = new User();
		newUser.setName(registerRequest.name());
		newUser.setEmail(registerRequest.email());
		newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
		this.repository.save(newUser);

		return new AuthResponseDTO(newUser.getName(), tokenService.generateToken(newUser));
	}
}
