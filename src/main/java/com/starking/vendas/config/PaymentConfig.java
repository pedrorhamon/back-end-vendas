package com.starking.vendas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public com.starking.payment.PaymentService paymentService() {
        return com.starking.payment.StripePaymentService();
    }
}
