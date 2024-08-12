package com.starking.vendas.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@Service
@AllArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;

	public void sendEmail(String to, String subject,  String htmlContent) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlContent, true);

		mailSender.send(message);
	}
	
	public void sendWelcomeEmail(String to, String name) throws MessagingException {
	        String subject = "Bem-vindo!";
	        String htmlContent = "<!DOCTYPE html>"
	                + "<html lang='en'>"
	                + "<head>"
	                + "    <meta charset='UTF-8'>"
	                + "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
	                + "    <title>Bem-vindo!</title>"
	                + "    <style>"
	                + "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
	                + "        .container { width: 100%; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 20px auto; }"
	                + "        .header { background-color: #4CAF50; color: white; padding: 10px 0; text-align: center; border-radius: 8px 8px 0 0; }"
	                + "        .content { padding: 20px; color: #333333; }"
	                + "        .footer { text-align: center; padding: 10px 0; color: #777777; font-size: 12px; }"
	                + "        .button { display: inline-block; padding: 10px 20px; color: white; background-color: #4CAF50; border-radius: 5px; text-decoration: none; }"
	                + "    </style>"
	                + "</head>"
	                + "<body>"
	                + "    <div class='container'>"
	                + "        <div class='header'>"
	                + "            <h1>Bem-vindo, " + name + "!</h1>"
	                + "        </div>"
	                + "        <div class='content'>"
	                + "            <p>Olá, " + name + ",</p>"
	                + "            <p>Estamos muito felizes em ter você conosco. Obrigado por se juntar a nós!</p>"
	                + "            <p>Para começar, clique no botão abaixo:</p>"
	                + "            <p><a href='#' class='button'>Começar</a></p>"
	                + "            <p>Se você tiver alguma dúvida, sinta-se à vontade para nos contatar.</p>"
	                + "            <p>Atenciosamente,<br>Equipe da Empresa</p>"
	                + "        </div>"
	                + "        <div class='footer'>"
	                + "            <p>&copy; 2024 Starking. Todos os direitos reservados.</p>"
	                + "        </div>"
	                + "    </div>"
	                + "</body>"
	                + "</html>";

	        sendEmail(to, subject, htmlContent);
	    }
	
	 public void sendPasswordEmail(String to, String name, String password) throws MessagingException {
	        String subject = "Sua Senha de Acesso";
	        String htmlContent = "<!DOCTYPE html>"
	                + "<html lang='en'>"
	                + "<head>"
	                + "    <meta charset='UTF-8'>"
	                + "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
	                + "    <title>Sua Senha de Acesso</title>"
	                + "    <style>"
	                + "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
	                + "        .container { width: 100%; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 20px auto; }"
	                + "        .header { background-color: #4CAF50; color: white; padding: 10px 0; text-align: center; border-radius: 8px 8px 0 0; }"
	                + "        .content { padding: 20px; color: #333333; }"
	                + "        .footer { text-align: center; padding: 10px 0; color: #777777; font-size: 12px; }"
	                + "        .button { display: inline-block; padding: 10px 20px; color: white; background-color: #4CAF50; border-radius: 5px; text-decoration: none; }"
	                + "    </style>"
	                + "</head>"
	                + "<body>"
	                + "    <div class='container'>"
	                + "        <div class='header'>"
	                + "            <h1>Sua Senha de Acesso</h1>"
	                + "        </div>"
	                + "        <div class='content'>"
	                + "            <p>Olá, " + name + ",</p>"
	                + "            <p>Você solicitou o envio da sua senha de acesso. Abaixo está sua senha:</p>"
	                + "            <p style='font-weight: bold; font-size: 18px;'>Senha: " + password + "</p>"
	                + "            <p>Recomendamos que você altere esta senha após o primeiro acesso.</p>"
	                + "            <p><a href='#' class='button'>Acessar Conta</a></p>"
	                + "            <p>Se você não solicitou esta senha, por favor, ignore este e-mail ou entre em contato conosco.</p>"
	                + "            <p>Atenciosamente,<br>Equipe da Empresa</p>"
	                + "        </div>"
	                + "        <div class='footer'>"
	                + "            <p>&copy; 2024 Starking. Todos os direitos reservados.</p>"
	                + "        </div>"
	                + "    </div>"
	                + "</body>"
	                + "</html>";

	        sendEmail(to, subject, htmlContent);
	    }

}
