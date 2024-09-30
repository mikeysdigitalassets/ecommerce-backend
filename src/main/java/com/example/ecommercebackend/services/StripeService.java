package com.example.ecommercebackend.services;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;  // Initialize Stripe with the secret key
    }

    public PaymentIntent createPaymentIntent(Long amount) throws Exception {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd") // Customize the currency if needed
                .build();

        return PaymentIntent.create(createParams);
    }
}
