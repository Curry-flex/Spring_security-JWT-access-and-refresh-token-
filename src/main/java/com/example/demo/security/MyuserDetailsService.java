package com.example.demo.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entity.AppUser;
import com.example.demo.repository.userRepository;


public class MyuserDetailsService implements UserDetailsService {
      @Autowired
      private userRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 AppUser user  = userRepository.findByUsername(username);
		 
		 if(user == null)
		 {
			 System.out.println("No user records found for user " + username);
			 throw new UsernameNotFoundException("No user records found for user " + username);
		 }
		 else
		 {
			 System.out.println("Username found " + username);
		 }
		 
		 Collection<SimpleGrantedAuthority> authorties = new ArrayList<>();
		 user.getRole().forEach(role ->{
			 authorties.add(new SimpleGrantedAuthority(role.getname()));
		 });
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorties);
	}

}
