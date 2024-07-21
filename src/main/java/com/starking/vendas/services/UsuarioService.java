package com.starking.vendas.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.starking.vendas.model.Usuario;
import com.starking.vendas.model.request.UsuarioRequest;
import com.starking.vendas.model.response.UsuarioResponse;
import com.starking.vendas.repositories.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
		Page<Usuario> UsuarioPage = usuarioRepository.findAll(pageable);
		return UsuarioPage.map(UsuarioResponse::new);
	}
    
    public UsuarioResponse obterUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new UsuarioResponse(usuario);
    }
    
    public UsuarioResponse criarUsuario(UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setName(usuarioRequest.getName());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(usuarioRequest.getSenha());  // Ensure to encode the password in the service
        usuario.setAtivo(usuarioRequest.getAtivo());  
        usuario.setPermissoes(usuarioRequest.getPermissoes()); 
        usuario.setCreatedAt(LocalDateTime.now()); 

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new UsuarioResponse(usuarioSalvo);
    }

}
