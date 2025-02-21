package org.ibtissam.dadesadventures.service;

import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

public interface MailService {

    void sendHtmlEmail(String to, String subject, String templateName, Context context) throws MessagingException;
}
