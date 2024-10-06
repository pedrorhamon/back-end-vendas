package com.starking.vendas.repositories;

import com.starking.vendas.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testFindByEmail() {
        Usuario usuario = new Usuario();
        usuario.setName("Test User");
        usuario.setEmail("testuser@example.com");
        usuario.setSenha("password");
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail("testuser@example.com");

        assertTrue(usuarioOptional.isPresent());
        assertEquals("testuser@example.com", usuarioOptional.get().getEmail());
        assertEquals("Test User", usuarioOptional.get().getName());
    }

    @Test
    public void testFindByEmailNotFound() {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail("testuser@example.com");
        assertFalse(usuarioOptional.isPresent());
    }
}
