package com.example.demo;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Role;
import com.example.demo.service.userServiceImp;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(userServiceImp serviceImp) {
		
		return args -> {
			serviceImp.addRole(new Role(1, "ROLE_USER"));
			serviceImp.addRole(new Role(2,"ROLE_ADMIN"));
			serviceImp.addRole(new Role(3,"ROLE_MANAGER"));
			serviceImp.addRole(new Role(4,"ROLE_SUPER_ADMIN"));
			
			serviceImp.addUser(new AppUser(1,"Ally mtati","curryflex","12345",new ArrayList<>()));
			serviceImp.addUser(new AppUser(2,"Amos makala","makaladsm","12345",new ArrayList<>()));
			serviceImp.addUser(new AppUser(3,"Ayesha Ally","queenayesha","12345",new ArrayList<>()));
			
			serviceImp.addRoleToUser("curryflex","ROLE_USER");
			serviceImp.addRoleToUser("curryflex","ROLE_ADMIN");
			serviceImp.addRoleToUser("curryflex","ROLE_SUPER_ADMIN");
			serviceImp.addRoleToUser("makaladsm","ROLE_MANAGER");
			serviceImp.addRoleToUser("queenayesha","ROLE_ADMIN");
		};
	}

}
