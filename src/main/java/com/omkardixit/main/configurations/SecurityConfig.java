package com.omkardixit.main.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.formLogin(formLogin -> formLogin.loginPage("/login"));
		http.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")));
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/login").permitAll()
				.requestMatchers("/signup").permitAll()
				.requestMatchers("/error").permitAll()
				.requestMatchers("/").permitAll()
				.requestMatchers("/stream/**").permitAll()
				.requestMatchers("/video/**").permitAll()
				.requestMatchers("/channel/**").permitAll()
				.requestMatchers("/ws-youtube-clone/**").permitAll()
				.requestMatchers("/scripts/**").permitAll()
				.requestMatchers("/styles/**").permitAll()
				.anyRequest().authenticated()
				);
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
