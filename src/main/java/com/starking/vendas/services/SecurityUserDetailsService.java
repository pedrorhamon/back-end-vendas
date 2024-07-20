package com.starking.vendas.services;

import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Usuario;
import com.starking.vendas.repositories.UsuarioRepository;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService{
	
	private final UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuarioEncontrado = usuarioRepository
				.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Email não cadastrado"));
		
		return User.builder()
				.username(usuarioEncontrado.getEmail())
				.password(usuarioEncontrado.getSenha())
				.build();
	}
}
