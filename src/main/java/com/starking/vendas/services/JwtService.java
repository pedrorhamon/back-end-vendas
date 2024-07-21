package com.starking.vendas.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.starking.vendas.config.JwtProperties;
import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.response.UsuarioResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class JwtService {

//	@Value("${jwt.expiracao}")
//	private String expiracao = "30";
//
//	@Value("${jwt.chave-assinatura}")
//	private String chaveAssinatura;
//	
//	@Value("${jwt.refresh-expiracao}")
//	private String refreshExpiracao;
	
	private final JwtProperties jwtProperties;

	public String gerarToken(UsuarioResponse usuarioResponse) {
		long exp = Long.valueOf(jwtProperties.getExpiracao());
		LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
		Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
		java.util.Date data = Date.from(instant);

		String horaExpiracaoToken = dataHoraExpiracao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		
		List<String> roles = usuarioResponse.getPermissoes().stream()
                .map(Permissao::getName) 
                .collect(Collectors.toList());

		String token = Jwts.builder()
				.setExpiration(data).setSubject(usuarioResponse.getEmail())
				.claim("userid", usuarioResponse.getId()).claim("name", usuarioResponse.getName())
				.claim("roles", roles)
				.claim("horaExpiracao", horaExpiracaoToken).signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtProperties.getChaveAssinatura())
				.compact();

		return token;
	}

	public Claims obterClaims(String token) throws ExpiredJwtException {
		return Jwts.parser().setSigningKey(jwtProperties.getChaveAssinatura()).parseClaimsJws(token).getBody();
	}

	public boolean isTokenValido(String token) {
		try {
			Claims claims = obterClaims(token);
			java.util.Date dataEx = claims.getExpiration();
			LocalDateTime dataExpiracao = dataEx.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			boolean dataHoraAtualIsAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
			return !dataHoraAtualIsAfterDataExpiracao;
		} catch (ExpiredJwtException e) {
			return false;
		}
	}

	public String obterLoginUsuario(String token) {
		Claims claims = obterClaims(token);
		return claims.getSubject();
	}
	
	 public String gerarRefreshToken(UsuarioResponse usuario) {
	        long exp = Long.valueOf( jwtProperties.getRefreshExpiracao());
	        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
	        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
	        Date data = Date.from(instant);
	        
	        List<String> roles = usuario.getPermissoes().stream()
	                .map(Permissao::getName) 
	                .collect(Collectors.toList());

	        String refreshToken = Jwts
	                .builder()
	                .setExpiration(data)
	                .setSubject(usuario.getEmail())
	                .claim("userid", usuario.getId())
	                .claim("roles", roles)
	                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtProperties.getChaveAssinatura())
	                .compact();

	        return refreshToken;
	 }
	 
	 public boolean isRefreshTokenValido(String refreshToken) {
	        try {
	            Claims claims = obterClaims(refreshToken);
	            Date dataEx = claims.getExpiration();
	            LocalDateTime dataExpiracao = dataEx.toInstant()
	                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
	            boolean dataHoraAtualIsAfterDataExpiracao = LocalDateTime.now().isAfter(dataExpiracao);
	            return !dataHoraAtualIsAfterDataExpiracao;
	        } catch (ExpiredJwtException e) {
	            return false;
	        }
	    }

}
