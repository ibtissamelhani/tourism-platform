package org.ibtissam.dadesadventures.service;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException;
}
