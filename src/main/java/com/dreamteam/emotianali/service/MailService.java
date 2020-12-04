package com.dreamteam.emotianali.service;

import com.dreamteam.emotianali.config.MailConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailConfig mailConfig;

    public boolean send(String to, String theme, String message) {
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
            switch (theme) {
                case "password_change":
                    return sendPasswordReset(email, message);
                case "role_change":
                    return sendRoleChanged(email, message);
                case "registration_confirm":
                    return sendRegistrationConfirm(email, message);
                default:
                    return false;
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
                    "<p>Dreamteam <3</p>\n" +
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
                    "<p>Dreamteam <3</p>\n" +
                    "</body>\n" +
                    "</html>");
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendRegistrationConfirm(HtmlEmail email, String name) {
        try {
            email.setSubject("Welcome to Emotianali!");
            email.setHtmlMsg("<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>" + name + ", you signed up at Emotianali.</h1\n" +
                    "<p>Heads of universe appreciate and greet you.</p>\n" +
                    "<a href=\"https://emotianali.herokuapp.com/emotional_assessment/start\">Start emotional assessment</a>\n" +
                    "<p>Dreamteam <3</p>\n" +
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