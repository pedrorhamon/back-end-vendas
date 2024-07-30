package com.starking.vendas.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.starking.vendas.config.JwtTokenFilter;
import com.starking.vendas.model.Usuario;
import com.starking.vendas.model.request.UsuarioRequest;
import com.starking.vendas.model.response.UsuarioResponse;
import com.starking.vendas.repositories.UsuarioRepository;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtTokenFilter jwtTokenFilter;
    
    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
		Page<Usuario> usuarioPage = usuarioRepository.findAll(pageable);
		return usuarioPage.map(UsuarioResponse::new);
	}
    
    public UsuarioResponse obterUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
//        UsuarioResponse dto = new UsuarioResponse();
//        dto.setId(usuario.getId());
//        dto.setEmail(usuario.getEmail());
//        dto.setPermissoes(usuario.getPermissoes().stream()
//                .map(Permissao::getName)
//                .collect(Collectors.toList()));
//
//        return dto;
//        usuario.getPermissoes().size();
        return new UsuarioResponse(usuario);
    }
    
    public UsuarioResponse autenticar(String email, String senha, String recaptchaToken) {
    	
//		if (recaptchaToken != null && !recaptchaToken.isEmpty()) {
			boolean isRecaptchaValid = jwtTokenFilter.verifyRecaptcha(recaptchaToken);
			if (!isRecaptchaValid) {
				throw new RuntimeException("reCAPTCHA inválido");
			}
//		}
    	
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        
        if (!usuarioOptional.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }
        
        Usuario usuario = usuarioOptional.get();
        boolean senhasBatem = passwordEncoder.matches(senha, usuario.getSenha());
        
        if (!senhasBatem) {
            throw new EntityNotFoundException("password invalid");
        }

        // Return a UsuarioResponse instead of Usuario
        return new UsuarioResponse(usuario);
    }
    
    public UsuarioResponse criarUsuario(UsuarioRequest usuarioRequest) {
        
        validarEmail(usuarioRequest.getEmail());
        
        criptografarSenha(usuarioRequest);
        
        Usuario usuario = new Usuario();
        usuario.setName(usuarioRequest.getName());
        usuario.setEmail(usuarioRequest.getEmail()); 
        usuario.setSenha(usuarioRequest.getSenha()); 
        usuario.setAtivo(usuarioRequest.getAtivo());
        usuario.setPermissoes(usuarioRequest.getPermissoes());
        usuario.setCreatedAt(LocalDateTime.now());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        // Envie o email de boas-vindas
        try {
            emailService.sendWelcomeEmail(usuarioRequest.getEmail(), usuarioRequest.getName());
        } catch (MessagingException e) {
            e.printStackTrace(); // Trate o erro apropriadamente na produção
        }

        return new UsuarioResponse(usuarioSalvo);
    }
    
    public UsuarioResponse atualizarUsuario(Long usuarioId, UsuarioRequest usuarioRequest) {
    	
        Usuario usuarioExistente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (usuarioRequest.getEmail() != null && !usuarioExistente.getEmail().equals(usuarioRequest.getEmail())) {
            validarEmail(usuarioRequest.getEmail());
            usuarioExistente.setEmail(usuarioRequest.getEmail());
        }

        if (usuarioRequest.getSenha() != null && !usuarioRequest.getSenha().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        }

        if (usuarioRequest.getName() != null) {
            usuarioExistente.setName(usuarioRequest.getName());
        }
        if (usuarioRequest.getAtivo() != null) {
            usuarioExistente.setAtivo(usuarioRequest.getAtivo());
        }
        if (usuarioRequest.getPermissoes() != null) {
            usuarioExistente.setPermissoes(usuarioRequest.getPermissoes());
        }
        
        usuarioExistente.setUpdatedAt(LocalDateTime.now());

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        return new UsuarioResponse(usuarioAtualizado);
    }

    
    private void criptografarSenha(UsuarioRequest usuarioRequest) {
        if (usuarioRequest == null || usuarioRequest.getSenha() == null || usuarioRequest.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String senhaCripto = passwordEncoder.encode(usuarioRequest.getSenha());
        usuarioRequest.setSenha(senhaCripto);
    }

    public void validarEmail(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);
        if (existe) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
    }
    
    @Transactional
    public void excluirUsuario(Long id) {
    	Usuario usuario = this.usuarioRepository.findById(id)
    	 .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    	usuarioRepository.delete(usuario);
    }
    
    @Transactional
    public UsuarioResponse desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pessoa not found with id " + id));

        usuario.setAtivo(false);
        Usuario usuarioDesativada = usuarioRepository.save(usuario);

        return new UsuarioResponse(usuarioDesativada);
    }

}
