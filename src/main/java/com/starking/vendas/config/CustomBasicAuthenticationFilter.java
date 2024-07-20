package com.starking.vendas.config;

import java.io.IOException;
import java.util.Base64;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.starking.vendas.model.Usuario;
import com.starking.vendas.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author pedroRhamon
 */
@Component
@RequiredArgsConstructor
public class CustomBasicAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";
    private final UsuarioRepository usuarioRepository;

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isBasicAuthentication(request)){
            String[] credentials = decodeBase64(getHeader(request).replace(BASIC, ""))
                    .split(":");

            String username = credentials[0];
            String password = credentials[1];

            Usuario usuario = usuarioRepository.findByNameFetchPermissoes(username);

            if(usuario == null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("User does not exist!");
                return;
            }

            boolean valid = checkPassword(usuario.getSenha(), password);

            if(!valid){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Password not match");
                return;
            }

            setAuthentication(usuario);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Usuario usuario) {
        Authentication authentication = createAuthenticationToken(usuario);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthenticationToken(Usuario usuario) {
        UserPrincipal userPrincipal = UserPrincipal.create(usuario);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }

    private boolean checkPassword(String userPassword, String loginPassword) {
        return passwordEncoder().matches(loginPassword, userPassword);
    }

    private String decodeBase64(String base64) {
        byte[] decodeBytes = Base64.getDecoder().decode(base64);
        return new String(decodeBytes);
    }

    private boolean isBasicAuthentication(HttpServletRequest request) {
        String header = getHeader(request);
        return header != null && header.startsWith(BASIC);
    }

    private String getHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }
}
