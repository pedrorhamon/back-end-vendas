package com.starking.vendas;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author pedroRhamon
 */
public class Gerador {
	
	 public static void main(String[] args) {
	        SecureRandom secureRandom = new SecureRandom();
	        byte[] key = new byte[32]; // 256 bits
	        secureRandom.nextBytes(key);
	        String base64Key = Base64.getEncoder().encodeToString(key);
	        System.out.println("Chave secreta base64: " + base64Key);
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
