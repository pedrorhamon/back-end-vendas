package com.starking.vendas.services;

import com.starking.vendas.config.JwtTokenFilter;
import com.starking.vendas.model.Usuario;
import com.starking.vendas.model.response.UsuarioResponse;
import com.starking.vendas.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtTokenFilter jwtTokenFilter;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Usuario usuario1 = new Usuario();
        usuario1.setName("Teste");

        Usuario usuario2 = new Usuario();
        usuario2.setName("Teste2");

        Page<Usuario> usuarioPage = new PageImpl<>(Arrays.asList(usuario1, usuario2));

        when(repository.findAll(pageable)).thenReturn(usuarioPage);

        Page<UsuarioResponse> result = usuarioService.listarTodos(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Teste", result.getContent().get(0).getName());
        assertEquals("Teste2", result.getContent().get(1).getName());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.obterUsuarioPorId(id);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testObterUsuarioPorIdNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.obterUsuarioPorId(id);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testAutenticarUsuarioComSucesso() {
        String email = "email@email.com";
        String password = "password";
        String recaptcha = "valid-recaptcha-token";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(password));

        when(jwtTokenFilter.verifyRecaptcha(recaptcha)).thenReturn(true);
        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(password, usuario.getSenha())).thenReturn(true);

        UsuarioResponse result = usuarioService.autenticar(email, password, recaptcha);
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testAutenticarUsuarioRecaptchaInvalido() {
        String email = "email@email.com";
        String password = "password";
        String recaptcha = "invalid-recaptcha-token";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(password));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.autenticar(email, password, recaptcha);
        });

        assertEquals("reCAPTCHA inválido", exception.getMessage());
    }

    @Test
    public void testAutenticarUsuarioSenhaInvalida() {
        String email = "user@example.com";
        String senha = "password";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode("outra-senha"));

        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.autenticar(email, senha, null);
        });

        assertEquals("password invalid", exception.getMessage());
    }
}
