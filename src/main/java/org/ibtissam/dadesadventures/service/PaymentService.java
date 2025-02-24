package org.ibtissam.dadesadventures.service;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {
    PaymentIntent createPaymentIntent(Long amount, String currency, String description) throws StripeException;
}
