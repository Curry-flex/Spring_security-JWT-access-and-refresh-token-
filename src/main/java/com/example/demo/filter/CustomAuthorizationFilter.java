package com.example.demo.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.demo.entity.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
//Interceot every request comming in for token verification
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//check if request is login
		if(request.getServletPath().equals("/login") || request.getServletPath().equals("/api/v1/refreshToken"))
		{
			filterChain.doFilter(request, response);
		}
		else {
			String authorizationHeader = request.getHeader("Authorization");
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			   try {
					String token =authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); 
					//verify token
					JWTVerifier verifier =JWT.require(algorithm).build();
					DecodedJWT decodedJWT = verifier.verify(token);
				    String username =decodedJWT.getSubject();
				    String [] roles =decodedJWT.getClaim("roles").asArray(String.class);
				    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
				     
				    for(String role: roles)
				    {
				    	authorities.add(new SimpleGrantedAuthority(role));
				    }
				     
				    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
				    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				    
				    //pass request to continue
				    filterChain.doFilter(request, response);
				    
					} catch (Exception e) {
						System.out.println(e.getMessage());
						response.setHeader("EROR", e.getMessage());
						
						Map<String,String> error = new HashMap<>();
						error.put("error", e.getMessage());
						response.setContentType("application/json");
						new ObjectMapper().writeValue(response.getOutputStream(), error);
						
					}
			}
			
			else {
				filterChain.doFilter(request, response);
			}
			
		}
		
	}

}
