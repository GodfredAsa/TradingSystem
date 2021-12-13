package com.clientService;

import com.clientService.securityConfig.JWTUtility;
import com.clientService.user.model.*;
import com.clientService.user.repository.AccountRepository;
import com.clientService.user.repository.AppUserRepository;
import com.clientService.user.repository.PortfolioRepository;
import com.clientService.user.service.AppUserAuthService;
import com.clientService.user.service.AppUserSignInService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class ClientServiceApplicationTests {
	@MockBean
	AppUserRepository appUserRepository;

	@MockBean
	AccountRepository accountRepository;

	@MockBean
	PortfolioRepository portfolioRepository;

	@Autowired
	AppUserAuthService appUserAuthService;
	
	@Autowired
	AppUserSignInService appUserSignInService;

	UserSignUp user;

	@Autowired
	HttpResponseBody response;

	@Autowired
	JwtRequest jwtRequest;

	@Autowired
	JWTUtility jwtUtility;

	@Autowired
	AuthenticationManager authenticationManager;

	@MockBean
	JmsTemplate jmsTemplate;

	@BeforeEach
	public void setUp(){
		Mockito.when(appUserRepository.getCountOfEmail(anyString()))
				.thenReturn(0);

		Mockito.when(appUserRepository.save(any()))
				.thenReturn(new AppUser());

		Mockito.when(accountRepository.save(any()))
				.thenReturn(new Account());

		Mockito.when(portfolioRepository.save(any()))
				.thenReturn(new Portfolio());

		Mockito.doNothing().when(jmsTemplate)
				.convertAndSend(anyString(),(Object) any());

		Mockito.when(authenticationManager.authenticate(any())).thenReturn(any());

		Mockito.when(appUserAuthService.loadUserByUsername(anyString()))
				.thenReturn(any());

		Mockito.when(jwtUtility.generateToken(any())).thenReturn("abde.xyz.abc");

		Mockito.when(appUserRepository.getAppUserByEmail(anyString())).thenReturn(new AppUser());

		user = new UserSignUp("rich","qua",
				"1234", "rich@gmail.com");

		jwtRequest.setEmail("rixh@gmail.com");
		jwtRequest.setPassword("123");

		response.setMessage("User added successfully");
	}

	@Test
	public void testRegister(){
		ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.CREATED);
		ResponseEntity<?> actual = appUserAuthService.registerUser(user, "client");

		assertEquals(expected, actual);
	}

	@Test
	public void testAuthenticateUser(){
		Map<String, Object> responseBody =  new HashMap<>();
		responseBody.put("token", "abde.xyz.abc");
		responseBody.put("user", new AppUser());

		response.setMessage(responseBody);
		ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.FOUND);
		ResponseEntity<?> actual = appUserSignInService.authenticateUser(jwtRequest);

		assertEquals(expected, actual);
	}

}
