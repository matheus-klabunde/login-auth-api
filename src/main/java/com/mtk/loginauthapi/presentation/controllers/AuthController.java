package com.mtk.loginauthapi.presentation.controllers;

import com.mtk.loginauthapi.application.services.AuthService;
import com.mtk.loginauthapi.presentation.dto.LoginRequestDTO;
import com.mtk.loginauthapi.presentation.dto.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController
{
	private final AuthService service;

	@Autowired
	public AuthController(AuthService service)
	{
		this.service = service;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest)
	{
		try
		{
			return ResponseEntity.ok(service.login(loginRequest));
		}
		catch (Exception e)
		{
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest)
	{
		try
		{
			return ResponseEntity.ok(service.register(registerRequest));
		}
		catch (Exception e)
		{
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
