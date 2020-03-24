package com.uxpsystems.assignment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.uxpsystems.assignment.dao.UserRepository;

@SpringBootConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
@ComponentScan({"com.uxpsystems.assignment.service,com.uxpsystems.assignment.dao"})
public class ApplicationConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAuthenticationEntryPoint userAuthenticationEntryPoint;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		//All the apis need to be authenticated with username and password
		auth.userDetailsService(userDetailsService).passwordEncoder(
				passwordEncoder());
		
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		System.out.println(" Inside WebSecurityConfig:passwordEncoder");
		return new BCryptPasswordEncoder();
	}
	 String roles [] = new String[] {"USER", "ADMIN" };
	 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println(" Inside WebSecurityConfig:configure http security");
		
		//Configuring http for basic authentication
		http.requestCache()
        .requestCache(new NullRequestCache()).and().
		cors().and().csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.POST,"/assignment/users").permitAll()
				.anyRequest().authenticated()
			//	.antMatchers(HttpMethod.DELETE,"/assignment/users").denyAll()
			//	.antMatchers(HttpMethod.DELETE,"/assignment/users").hasRole("ADMIN")
				.and()
				.httpBasic().realmName("TEST REALM")
				.authenticationEntryPoint(userAuthenticationEntryPoint).and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
