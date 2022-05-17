package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.Role;
import com.example.demo.service.userServiceImp;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("api/v1/")
public class UserController {
   
	@Autowired
	private userServiceImp userServiceImp;
	
	@GetMapping("users")
	public ResponseEntity<List<AppUser>> getUsersList()
	{
	
		return ResponseEntity.ok().body(userServiceImp.getAllUsers());
	}
	
	@PostMapping("addUser")
	ResponseEntity<AppUser> addNewUser(@RequestBody AppUser appUser)
	{
		return ResponseEntity.ok().body(userServiceImp.addUser(appUser));
	}
	
	@PostMapping("addRole")
	ResponseEntity<Role> addRole(@RequestBody Role role)
	{
		return ResponseEntity.ok().body(userServiceImp.addRole(role));
		
	}
	
	@PostMapping("addRoleToUser")
	ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form)
	{
		
		 userServiceImp.addRoleToUser(form.getUsername() ,form.getRoleName());
		  return ResponseEntity.ok().build();
	}
	
	//REFRESH TOKEN
	@GetMapping("refreshToken")
     public   void refreshToken(HttpServletRequest request ,HttpServletResponse response) throws  IOException
	{
	   String authorizationHeader = request.getHeader("Authorization");
	   
	   if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		   try {
				String refresh_token =authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); 
				//verify token
				JWTVerifier verifier =JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
			    String username =decodedJWT.getSubject();
			    
			    //find user of the system
			    AppUser user = userServiceImp.getUser(username);
			   
				String access_token =JWT.create()
				         .withSubject(user.getUsername())
				         .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
				         .withClaim("roles",user.getRole().stream().map(Role::getname).collect(Collectors.toList()))
				         .sign(algorithm);
	
		
					//Get JWT in body
					Map<String,String> token = new HashMap<>();
					token.put("access_token", access_token);
					token.put("refresh_token", refresh_token);
					
					
					response.setContentType("application/json");
					new ObjectMapper().writeValue(response.getOutputStream(), token);
			 
			    
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
			throw new RuntimeException("refresh token is missing");
		}
	}
}

 class RoleToUserForm{
	 private String username;
	 private String roleName;
	 
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	 
	 
	 
	 
 }
