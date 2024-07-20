package com.starking.vendas.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.starking.vendas.model.Usuario;

import lombok.Getter;

/**
 * @author pedroRhamon
 */
@Getter
public class UserPrincipal {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Usuario usuario){
        this.username = usuario.getEmail();
        this.password = usuario.getSenha();

        this.authorities = usuario.getPermissoes().stream().map(role -> {
            return new SimpleGrantedAuthority("ROLE_".concat(role.getName()));
        }).collect(Collectors.toList());
    }

    public static UserPrincipal create(Usuario usuario){
        return new UserPrincipal(usuario);
    }
}
