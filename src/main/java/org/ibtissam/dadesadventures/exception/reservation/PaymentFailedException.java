package org.ibtissam.dadesadventures.exception.reservation;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String message) {
        super(message);
    }
}
