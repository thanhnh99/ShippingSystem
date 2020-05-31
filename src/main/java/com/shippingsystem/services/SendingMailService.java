package com.shippingsystem.services;

import com.shippingsystem.models.MailProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

;

@Data
@Service
public class SendingMailService {

    private final MailProperties mailProperties;
    private final freemarker.template.Configuration templates;

    @Autowired
    public SendingMailService(MailProperties mailProperties, Configuration templates) {
        this.mailProperties = mailProperties;
        this.templates = templates;
    }

    public boolean sendVerificationMail(String toEMail, String verificationCode) {
        String subject = "Please verify your email";
        String body = "";

        try {
            Template t = templates.getTemplate("email-verification.ftl");
            Map<String, String> map = new HashMap<>();
            map.put("VERIFICATION_URL", mailProperties.getVerificationApi() + verificationCode);
            body = FreeMarkerTemplateUtils.processTemplateIntoString(t,map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return sendMail(toEMail, subject, body);
    }

    public boolean sendPasswordResetMail(String toEmail, String passwordForgotToken, String url){
        String subject = "Your password reset request";
        String body = "";

        try {
            Template resetPasswordTemplate = templates.getTemplate("email-passwordreset.ftl");
            Map<String, String> map = new HashMap<>();
            map.put("TO_EMAIL", toEmail);
            map.put("PASSWORD_RESET_URL", url + "/verifying-reset-password?token=" + passwordForgotToken);
            body = FreeMarkerTemplateUtils.processTemplateIntoString(resetPasswordTemplate, map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return sendMail(toEmail, subject, body);
    }

    private boolean sendMail(String toEmail, String subject, String content){
        Properties prop = new Properties();
        prop.put("mail.smtp.host", mailProperties.getSmtp().getHost());
        prop.put("mail.smtp.port", mailProperties.getSmtp().getPort());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailProperties.getSmtp().getUsername(), mailProperties.getSmtp().getPassword());
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailProperties.getFrom()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
