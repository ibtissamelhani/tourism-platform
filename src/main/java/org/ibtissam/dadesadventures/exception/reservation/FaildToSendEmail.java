package org.ibtissam.dadesadventures.exception.reservation;

public class FaildToSendEmail extends RuntimeException {
    public FaildToSendEmail(String message) {
        super(message);
    }
}
