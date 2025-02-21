package org.ibtissam.dadesadventures.service.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.ibtissam.dadesadventures.service.MailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngineService templateEngineService;

    @Override
    public void sendHtmlEmail(String to, String subject, String templateName, Context context) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);

        // Process the Thymeleaf template
        String htmlContent = templateEngineService.processTemplate(templateName, context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

}
