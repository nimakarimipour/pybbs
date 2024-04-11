package co.yiiu.pybbs.config.service;

import co.yiiu.pybbs.model.SystemConfig;
import co.yiiu.pybbs.service.ISystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
// aaaaaaaaa：https://www.cnblogs.com/whgk/p/6506027.html
@Component
@DependsOn("mybatisPlusConfig")
public class EmailService implements BaseService<Session> {

    @Resource
    private ISystemConfigService systemConfigService;

    private Session session;
    private final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Override
    public Session instance() {
        // aasessionaaaaa，aaaaa，aaaaaa
        if (session != null) return session;
        // sessionaa，aaaaaaaaaaaaaaaa，aaaaa，aaaaa
        SystemConfig systemConfigHost = systemConfigService.selectByKey("mail_host");
        String host = systemConfigHost.getValue();
        SystemConfig systemConfigUsername = systemConfigService.selectByKey("mail_username");
        String username = systemConfigUsername.getValue();
        SystemConfig systemConfigPassword = systemConfigService.selectByKey("mail_password");
        String password = systemConfigPassword.getValue();
        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) return null;
        Properties properties = new Properties();
        properties.setProperty("mail.host", host);
        //aaaaaaaa。
        properties.setProperty("mail.smtp.auth", "true");
        //0.2aaaa（aaaaa）
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        //1 aaaa
    /*
      props：aaaaaaaaa，Propertiesaa
              aaaaaaaaa、aaaaaaaaaa(aaaaaa)a
      authenticator：aaaa(aaaaa)
      aaaaaaaaaaaaaa。
     */
        this.session = Session.getDefaultInstance(properties, authenticator);
        return this.session;
    }

    public boolean sendEmail(String email, String title, String content) {
        // aaasessionaaaaaa，aaaaaaa
        if (this.instance() == null) return false;
        try {
            //2 aaaa
            Message message = new MimeMessage(this.session);
            String from = systemConfigService.selectAllConfig().get("mail_username").toString();
            // 2.1 aaa xxx@163.com aaaaaaaaa，aaaa
            message.setFrom(new InternetAddress(from));
      /*
        2.2 aaa
          aaaaa：
            RecipientType.TO    aaaaa
            RecipientType.CC    aa
            RecipientType.BCC    aa
          aaAaaBaaa，aaAaaaaaaaaCaaaaaa，aaaBaaaa，
          aaaaaaaaC，aaCaaaaaaaa，aaBaaaaAaCaaaaaaa
          aaaaaa(aa)aCaa，aaBaaaaAaCaaaaaaa。
          aaaaa
            aaaaaa，aaaaaAddress[]，aaaaaaaaaaaaa。aaaaaa。aaaaaaaaaaa，aaaaaaa
            aaaaaaaaaaqqaa
       */
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3 aa（aa）
            message.setSubject(title + " - " + systemConfigService.selectAllConfig().get("name").toString());
            // 2.4 aa
            //aaaa，aaaaaaaaaaa。
            message.setContent(content, "text/html;charset=UTF-8");
            //3aaaa
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
