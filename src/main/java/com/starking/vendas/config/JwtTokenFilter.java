package com.starking.vendas.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.RestTemplate;
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
	
	private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	private static final String RECAPTCHA_SECRET = "e-eCjkA_sQDHPMB0";
;

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
		
		String recaptchaToken = request.getParameter("recaptchaToken");

        if (recaptchaToken != null && !verifyRecaptcha(recaptchaToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid reCAPTCHA");
            return;
        }

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
	
	public boolean verifyRecaptcha(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format("secret=%s&response=%s", RECAPTCHA_SECRET, recaptchaResponse);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> recaptchaResponseEntity = restTemplate.exchange(RECAPTCHA_VERIFY_URL, HttpMethod.POST, entity, Map.class);
        Map<String, Object> recaptcha = (Map<String, Object>) recaptchaResponseEntity.getBody();

        return recaptcha != null && (Boolean) recaptcha.get("success");
    }
}