package com.shippingsystem.models.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    @Data
    public static class SMTP {
        String host;
        String port;
        String username;
        String password;

    }

    private SMTP smtp;
    private String from;
    private String fromName;
    private String verificationApi;
    private String passworsResetUrl;

}
