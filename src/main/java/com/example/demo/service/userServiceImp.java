package com.example.demo.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.AppUser;
import com.example.demo.entity.Role;
import com.example.demo.repository.roleRepository;
import com.example.demo.repository.userRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;

@Service
@Transactional
@Slf4j
@XSlf4j
public class userServiceImp implements UserService {
     @Autowired
     private userRepository userRepository;
     
     @Autowired
     private PasswordEncoder passwordEncoder;
     
     @Autowired
     private roleRepository roleRepository;
     
	@Override
	public AppUser addUser(AppUser appUser) {
	    
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		return userRepository.save(appUser);
	}

	@Override
	public Role addRole(Role role) {
		// TODO Auto-generated method stub
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser appuser= userRepository.findByUsername(username);
		Role role  =roleRepository.findByName(roleName);
		
		appuser.getRole().add(role);
	}

	@Override
	public AppUser getUser(String username) {
		// TODO Auto-generated method stub
		return userRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

}
