package com.example.demo.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private  AuthenticationManager authenticationManager;
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username =request.getParameter("username");
		String password = request.getParameter("password");
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenticationToken); //call authenticationmanager to authenticate user 
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		  User user =(User) authentication.getPrincipal(); //get logged in user
		//Genereate json web token for logged in suer
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //algorithm to sign json web token
		
		//create access token
		String access_token =JWT.create()
				         .withSubject(user.getUsername())
				         .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
				         .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				         .sign(algorithm);
		
		//create refresh token
		String refresh_token =JWT.create()
		         .withSubject(user.getUsername())
		         .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
		         .sign(algorithm);
		//set token in headers fro response
//		response.setHeader("access_token", access_token);
//		response.setHeader("refresh_token", refresh_token);
		
		//Get JWT in body
		Map<String,String> token = new HashMap<>();
		token.put("access_token", access_token);
		token.put("refresh_token", refresh_token);
		
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), token);
		
		
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.unsuccessfulAuthentication(request, response, failed);
	}
	
	

	 
}
