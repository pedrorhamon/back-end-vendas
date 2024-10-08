package com.starking.vendas;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author pedroRhamon
 */
public class Gerador {
	
//	 public static void main(String[] args) {
//	        SecureRandom secureRandom = new SecureRandom();
//	        byte[] key = new byte[32]; // 256 bits
//	        secureRandom.nextBytes(key);
//	        String base64Key = Base64.getEncoder().encodeToString(key);
//	        System.out.println("Chave secreta base64: " + base64Key);
//	    }

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String senha = "123456";
		System.out.println("Senha em hash (SHA-256): " + gerarHash(senha));
	}

	public static String gerarHash(String senha) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = md.digest(senha.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte b : hashBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	 
//	 public static void main(String[] args) {
//	        SecureRandom secureRandom = new SecureRandom();
//	        byte[] key = new byte[32]; // 256 bits
//	        secureRandom.nextBytes(key);
//	        
//	        // Convert byte array to hexadecimal string
//	        String hexKey = HexFormat.of().formatHex(key);
//	        System.out.println("Chave secreta hexadecimal: " + hexKey);
//	    }

}
