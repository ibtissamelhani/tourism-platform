package org.ibtissam.dadesadventures.exception.reservation;

public class FailedToSendEmailException extends RuntimeException {
    public FailedToSendEmailException(String message) {
        super(message);
    }
}
