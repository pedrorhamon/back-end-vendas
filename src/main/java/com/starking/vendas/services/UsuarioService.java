package com.starking.vendas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.starking.vendas.model.request.AlterarSenhaRequest;
import static com.starking.vendas.utils.MessagesUtils.*;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.starking.vendas.config.JwtTokenFilter;
import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.Usuario;
import com.starking.vendas.model.request.PermissaoRequest;
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
    private final JwtService jwtService;

    public Page<UsuarioResponse> listarTodos(Pageable pageable) {
		Page<Usuario> usuarioPage = usuarioRepository.findAll(pageable);
		return usuarioPage.map(UsuarioResponse::new);
	}
    
    public UsuarioResponse obterUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USUARIO_NAO_ENCONTRADO));
        return new UsuarioResponse(usuario);
    }
    
    public UsuarioResponse autenticar(String email, String senha, String recaptchaToken) {
    	
		if (recaptchaToken != null && !recaptchaToken.isEmpty()) {
			boolean isRecaptchaValid = jwtTokenFilter.verifyRecaptcha(recaptchaToken);
			if (!isRecaptchaValid) {
				throw new RuntimeException(RECAPTCHA_INVALIDO);
			}
		}
    	
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        
        if (!usuarioOptional.isPresent()) {
            throw new EntityNotFoundException(USUARIO_NAO_ENCONTRADO);
        }
        
        Usuario usuario = usuarioOptional.get();
        boolean senhasBatem = passwordEncoder.matches(senha, usuario.getSenha());
        
        if (!senhasBatem) {
            throw new EntityNotFoundException(SENHA_INVALIDA);
        }

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

        usuario.setCreatedAt(LocalDateTime.now());
        List<Permissao> permissoes = convertPermissoes(usuarioRequest.getPermissoes());
        usuario.setPermissoes(permissoes);
        usuario.setPermissoes(convertPermissoes(usuarioRequest.getPermissoes()));

        // Usar o método convertendoPermissions para converter a lista de permissões
//        List<Permissao> permissoes = convertPermissoes(usuarioRequest.getPermissoes());
//        usuario.setPermissoes(permissoes);

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
                .orElseThrow(() -> new IllegalArgumentException(USUARIO_NAO_ENCONTRADO_ID));

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
        	usuarioExistente.setPermissoes(convertPermissoes(usuarioRequest.getPermissoes()));
        }
        
        usuarioExistente.setUpdatedAt(LocalDateTime.now());

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        return new UsuarioResponse(usuarioAtualizado);
    }
    
    public List<Permissao> convertPermissoes(List<PermissaoRequest> permissaoRequests) {
        return permissaoRequests.stream()
            .map(pr -> {
                Permissao permissao = new Permissao();
                permissao.setId(pr.getId());  // Definindo o ID
                permissao.setName(pr.getName());  // Definindo o nome
                // Adicione mais propriedades se necessário
                return permissao;
            })
            .collect(Collectors.toList());
    }
    
    
    private void criptografarSenha(UsuarioRequest usuarioRequest) {
        if (usuarioRequest == null || usuarioRequest.getSenha() == null || usuarioRequest.getSenha().isEmpty()) {
            throw new IllegalArgumentException(SENHA_NAO_PODE_SER_VAZIA);
        }
        String senhaCripto = passwordEncoder.encode(usuarioRequest.getSenha());
        usuarioRequest.setSenha(senhaCripto);
    }

    public void validarEmail(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);
        if (existe) {
            throw new IllegalArgumentException(EMAIL_JA_CADASTRADO);
        }
    }
    
    @Transactional
    public void excluirUsuario(Long id) {
    	Usuario usuario = this.usuarioRepository.findById(id)
    	 .orElseThrow(() -> new EntityNotFoundException(USUARIO_NAO_ENCONTRADO + id));
    	usuarioRepository.delete(usuario);
    }

    @Transactional
    public UsuarioResponse desativar(Long gestorId, Long usuarioId) {
        Usuario gestor = usuarioRepository.findById(gestorId)
                .orElseThrow(() -> new EntityNotFoundException(USUARIO_NAO_ENCONTRADO_ID + gestorId));

        Boolean isGestor = gestor.getPermissoes().stream()
                .anyMatch(permissao -> permissao.getName().equals("ADMIN"));

        if (isGestor) {
            throw new SecurityException(USUARIO_NAO_POSSUI_PERMISSAO);
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(USUARIO_NAO_ENCONTRADO_ID + usuarioId));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario);
    }

	public void esquecerSenha(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException(EMAIL_NAO_ENCONTRADO_CADASTRADO + email));
		
		String novaSenha = gerarSenhaTemporaria();
		try {
			String senhaHash = passwordEncoder.encode(novaSenha);
	        usuario.setSenha(senhaHash);
	        usuarioRepository.save(usuario);
			emailService.sendPasswordEmail(usuario.getEmail(), usuario.getName(), novaSenha);
		} catch (MessagingException e) {
			e.printStackTrace(); // Trate o erro apropriadamente
			throw new RuntimeException(VERIFICACAO_SENHA);
		}
	}
	
	private String gerarSenhaTemporaria() {
	    return UUID.randomUUID().toString().substring(0, 8);
	}

    public void alterarSenha(AlterarSenhaRequest alterarSenhaRequest, String token) {
        String email = jwtService.obterLoginUsuario(token);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USUARIO_NAO_ENCONTRADO_ID));

        if (!passwordEncoder.matches(alterarSenhaRequest.getSenhaAtual(), usuario.getSenha())) {
            throw new ValidationException(SENHA_INVALIDA);
        }

        if (!alterarSenhaRequest.getNovaSenha().equals(alterarSenhaRequest.getConfirmarNovaSenha())) {
            throw new ValidationException(COMPARACAO_SENHA);
        }

        usuario.setSenha(passwordEncoder.encode(alterarSenhaRequest.getNovaSenha()));
        usuarioRepository.save(usuario);

        jwtService.revokeToken(token);

    }

}
