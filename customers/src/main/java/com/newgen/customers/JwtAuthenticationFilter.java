package com.newgen.customers;

import java.io.IOException;
import java.util.Collections;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final String secretKey;
	public JwtAuthenticationFilter(String secretKey) {
		this.secretKey = secretKey;
	}
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
			) throws ServletException, IOException{
			final String authHeader = request.getHeader("Authorization");
			if(authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
				String token = authHeader.substring(7);
				Claims claims;
				try {
					claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
				}catch(Exception e) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
					return;
				}
				
				String username = claims.getSubject();
				
				if(username!=null) {
					SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()));
				}
				
				filterChain.doFilter(request, response);
	}
	
}
