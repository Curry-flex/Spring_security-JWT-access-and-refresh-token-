package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Role;

public interface UserService {

	 AppUser addUser(AppUser appUser);
	 Role addRole(Role role); 
	 void addRoleToUser(String username, String roleName);
	 AppUser getUser(String username);
	 
	 List<AppUser> getAllUsers();
	 
}
