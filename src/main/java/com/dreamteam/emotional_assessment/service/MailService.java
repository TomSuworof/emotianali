package com.dreamteam.emotional_assessment.service;

import com.dreamteam.emotional_assessment.config.MailConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailConfig mailConfig;

    public boolean send(String to, String link) {
        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//            mailMessage.setFrom(Objects.requireNonNull(mailMessage.getFrom()));
//            mailMessage.setTo(to);
//            mailMessage.setSubject("Восстановление пароля");
//            String text = "Вы отправили запрос на восстановление пароля. \n" +
//                    "Страница для сброса пароля: "
//                    + new URL(link) +
//                    "\nЕсли вы не отправляли запрос, проигнорируйте это письмо." +
//                    "\nКоманда Dreamteam.";
//            mailMessage.setText(text);
//
//
//            mailSender.send(mailMessage);
//            return true;
            HtmlEmail email = new HtmlEmail();
            email.setHostName(mailConfig.getHost());
            email.setSmtpPort(mailConfig.getPort());
            email.setAuthenticator(new DefaultAuthenticator(
                    mailConfig.getUsername(),
                    mailConfig.getPassword())
            );
            email.setSSLOnConnect(true);
            email.setFrom(mailConfig.getUsername());
            email.setCharset("utf-8");
            email.setSubject("Восстановление пароля");
            email.setHtmlMsg("<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>Вы отправили запрос на восстановление пароля.</h1\n" +
                    "<p>" +
                    "<a href=\"" + link + "\">Восстановить пароль</a>\n" +
                    "\n" +
                    "</p>\n" +
                    "<p>Если вы отправляли запрос, проигнорируйте письмо.</p>\n" +
                    "<p>Команда Dreamteam</p>\n" +
                    "</body>\n" +
                    "</html>");
            // set the alternative message
            email.addTo(to);
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }
}