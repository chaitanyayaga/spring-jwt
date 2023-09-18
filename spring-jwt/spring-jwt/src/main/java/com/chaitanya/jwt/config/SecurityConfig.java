package com.chaitanya.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.chaitanya.jwt.filter.JwtAuthenticationFilter;
import com.chaitanya.jwt.helper.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint point;
	@Autowired
	private JwtAuthenticationFilter filter;
	@Autowired
	private UserDetailsService userDetailsService;
	//@Autowired
	//private PasswordEncoder passwordEncoder;
	
//	 @Bean
//	 public UserDetailsService userDetailsService() {
//	        UserDetails admin = User.withUsername("Basant")
//	                .password(encoder.encode("Pwd1"))
//	                .roles("ADMIN")
//	                .build();
//	        UserDetails user = User.withUsername("John")
//	                .password(encoder.encode("Pwd2"))
//	                .roles("USER","ADMIN","HR")
//	                .build();
//	        return new InMemoryUserDetailsManager(admin, user);
//	        return new CustomUserDetailService();
//	    }


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	 http.csrf(csrf -> csrf.disable())
     .authorizeHttpRequests()
         .requestMatchers("/test","/users").authenticated()
         .requestMatchers("/auth/login", "/auth/user").permitAll()
         .anyRequest().authenticated()
         .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	     http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
