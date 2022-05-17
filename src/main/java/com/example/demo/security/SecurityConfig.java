package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.CustomAuthenticationFilter;
import com.example.demo.filter.CustomAuthorizationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
//      @Autowired
//      private UserDetailsService userDetailsService;
      
      
      
      @Autowired
      private BCryptPasswordEncoder passwordEncoder;
      
      @Bean
      public UserDetailsService userDetailsService() {
          return new MyuserDetailsService();
      }
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		  http.csrf().disable();
		  http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		  http.authorizeRequests().antMatchers("/login").permitAll();
		  http.authorizeRequests().antMatchers("/api/v1/refreshToken/**").permitAll();
		  http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/v1/users/**").hasAnyAuthority("ROLE_ADMIN");
		  http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/addUser/**").hasAnyAuthority("ROLE_ADMIN");
		  http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/addRole/**").hasAnyAuthority("ROLE_ADMIN");
		  http.authorizeRequests().anyRequest().authenticated();
		  http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean())); //authentication Filter
		  http.addFilterBefore(new CustomAuthorizationFilter(),UsernamePasswordAuthenticationFilter.class); //Authorization filter
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	  auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
	  
	}
   
	
	
}
