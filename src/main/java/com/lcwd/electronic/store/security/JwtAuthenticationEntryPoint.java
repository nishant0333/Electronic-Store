package com.lcwd.electronic.store.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


//this class run when an Unauthorized request try to access an Authenticated API
//in this case it throw exception and display message.
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
	
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		PrintWriter writer = response.getWriter();
		writer.println("Access Denied !! "+authException.getMessage());
		
	}

}
