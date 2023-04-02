package com.lcwd.electronic.store.config;

import java.util.Arrays;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.lcwd.electronic.store.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.security.JwtAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

	
	
//Here we hard coded user which is not recommended
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails normal = User.builder()
//		.username("Ankit")
//		.password(passwordEncoder().encode("ankit"))
//		.roles("NORMAL")		
//		.build();
//		
//		UserDetails admin = User.builder()
//				.username("Durgesh")
//				.password(passwordEncoder().encode("durgesh"))
//				.roles("ADMIN")
//				.build();
//		
//		
//		//inMemoryUserDetailsManager-is implementation of UserDetailService
//		
//		return new InMemoryUserDetailsManager(normal,admin);
//		
//	}

	
	
//form login Authentication
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		
//		http.authorizeRequests()
//		.anyRequest()
//		.authenticated()
//		.and()
//		.formLogin()
//		.loginPage("login.html")
//		.loginProcessingUrl("/process-url")
//		.defaultSuccessUrl("/dashboard")
//		.failureUrl("error")
//		.and()
//		.logout()
//		.logoutUrl("/logout");
//		
//		
//	return http.build();	
//	}
	
	
	//Basic Authentication 
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		
//		http
//		.csrf().disable()
//		.cors().disable()
//		.authorizeRequests()
//		.anyRequest()
//		.authenticated()
//		.and()
//		.httpBasic();
//		
//		
//		return http.build();
//	}
	
	
	
	// Jwt Authentication
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
		.csrf().disable()
		.cors().disable()
		.authorizeRequests()
		
		.antMatchers("/auth/login")
		.permitAll()
		.antMatchers(HttpMethod.POST,"/users")
		.permitAll()
		.antMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
		
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		
		return builder.getAuthenticationManager();
	}
	
	
	//CORS Configuration 
//	@Bean
//	public FilterRegistrationBean corsFilter() {
//		
//		 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        CorsConfiguration configuration = new CorsConfiguration();
//	        configuration.setAllowCredentials(true);
////	        configuration.setAllowedOrigins(Arrays.asList("https://domain2.com","http://localhost:4200"));
//	        configuration.addAllowedOriginPattern("*");
//	        configuration.addAllowedHeader("Authorization");
//	        configuration.addAllowedHeader("Content-Type");
//	        configuration.addAllowedHeader("Accept");
//	        configuration.addAllowedMethod("GET");
//	        configuration.addAllowedMethod("POST");
//	        configuration.addAllowedMethod("DELETE");
//	        configuration.addAllowedMethod("PUT");
//	        configuration.addAllowedMethod("OPTIONS");
//	        configuration.setMaxAge(3600L);
//	        source.registerCorsConfiguration("/**", configuration);
//
//	        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
//	        filterRegistrationBean.setOrder(-110);
//	        return filterRegistrationBean;
//
//	}
	
}
