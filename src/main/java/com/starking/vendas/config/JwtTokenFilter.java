package com.starking.vendas.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.starking.vendas.services.JwtService;
import com.starking.vendas.services.SecurityUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	private final SecurityUserDetailsService userDetailsService;

//	@Override
//	protected void doFilterInternal(
//			HttpServletRequest request, 
//			HttpServletResponse response, 
//			FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		String authorization = request.getHeader("Authorization");
//		
//		//"Bearer","eyJhbGciOiJIUzUxMiJ9.eyJ..."
//		
//		if(authorization != null && authorization.startsWith("Bearer")) {
//			
//			String token = authorization.split(" ")[1];
//			boolean isTokenValid = jwtService.isTokenValido(token);
//			
//			if(isTokenValid) {
//				String login = jwtService.obterLoginUsuario(token);
//				UserDetails usuarioAutenticado = userDetailsService.loadUserByUsername(login);
//				
//				UsernamePasswordAuthenticationToken user = 
//						new UsernamePasswordAuthenticationToken(
//								usuarioAutenticado, null, usuarioAutenticado.getAuthorities());
//				
//				user.setDetails( new WebAuthenticationDetailsSource().buildDetails(request) );
//				
//				SecurityContextHolder.getContext().setAuthentication(user);
//				
//			}
//		}
//		
//		filterChain.doFilter(request, response);
//	}
	
	@Override
	protected void doFilterInternal(
	        HttpServletRequest request, 
	        HttpServletResponse response, 
	        FilterChain filterChain)
	        throws ServletException, IOException {

	    String authorization = request.getHeader("Authorization");

	    if (authorization != null && authorization.startsWith("Bearer ")) { // Note o espaço após "Bearer"
	        String token = authorization.substring(7); // Extrai o token após "Bearer "
	        boolean isTokenValid = jwtService.isTokenValido(token);

	        if (isTokenValid) {
	            String login = jwtService.obterLoginUsuario(token);
	            UserDetails usuarioAutenticado = userDetailsService.loadUserByUsername(login);

	            UsernamePasswordAuthenticationToken user = 
	                    new UsernamePasswordAuthenticationToken(
	                            usuarioAutenticado, null, usuarioAutenticado.getAuthorities());

	            user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(user);
	        }
	    }

	    filterChain.doFilter(request, response);
	}
}