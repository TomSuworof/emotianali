package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.config.MailConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailConfig mailConfig;

    public boolean send(String to, boolean passwordReset, String theme) {
        try {
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
            email.addTo(to);
            if (passwordReset) {
                return sendPasswordReset(email, theme);
            } else {
                return sendRoleChanged(email, theme);
            }
        } catch (EmailException emailException) {
            return false;
        }
    }

    private boolean sendPasswordReset(HtmlEmail email, String link) {
        try {
            email.setSubject("Password reset");
            email.setHtmlMsg("<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>You sent request for password reset.</h1\n" +
                    "<p>" +
                    "<a href=\"" + link + "\">Reset password</a>\n" +
                    "\n" +
                    "</p>\n" +
                    "<p>If you did not send the request, ignore this message.</p>\n" +
                    "<p>Dreamteam</p>\n" +
                    "</body>\n" +
                    "</html>");
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendRoleChanged(HtmlEmail email, String role) {
        role = role.equals("blocked") ? role : "an " + role;
        try {
            email.setSubject("Your role was changed");
            email.setHtmlMsg("<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>Your role was changed.</h1\n" +
                    "<p>Heads of universe decided that you should be " + role + ".</p>\n" +
                    "<p>Dreamteam</p>\n" +
                    "</body>\n" +
                    "</html>");
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }
}