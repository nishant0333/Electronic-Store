package com.lcwd.electronic.store.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//Bearer 23234562szfdg
		
		String requestHeder = request.getHeader("Authorization");
		logger.info(" Header : {}",requestHeder);
		
		String username =null;
		String token = null;
		
		if(requestHeder !=null && requestHeder.startsWith("Bearer")) {
			//looking good
			
			token = requestHeder.substring(7);
			
			try {
				username = this.jwtHelper.getUsernameFromToken(token);
				
				
				
				
			} catch (IllegalArgumentException e) {
				
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
				
			}catch (ExpiredJwtException e) {
				
				logger.info("Given jwt is expired !!");
				e.printStackTrace();
				
			}catch (MalformedJwtException e) {
				
				logger.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
		}else {
			logger.info("Invalid Header Value !! ");
		}
		
		//
		if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			//fetch user detail from username
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
			if(validateToken) {
				//set authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				
			}else {
				logger.info("Validation failed !!");
			}
			
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}
