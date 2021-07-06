package com.spring.communication.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;

@Component
public class SmtpMailSender {

    @Value("${spring.mail.username}")
    String fromEmail;

    @Value("${EMAIL_SIGNATURE}")
    String emailSignature;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMailWithAttachment(
            String toAddresses, String subject, String body, MultipartFile[] files)
            throws MessagingException, IOException {

        log.info("Sending email with attachment(s)...");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);//true indicates multipart message
        helper.setSubject(subject);
        helper.setFrom(new InternetAddress(fromEmail));

        String[] toAddressList = toAddresses.split(",");

        for (String toAddress : toAddressList) {
            helper.addTo(new InternetAddress(toAddress));
        }
        log.info("Number of recipients : {}", toAddressList.length);

        MimeBodyPart mbp1 = new MimeBodyPart();
        body = body + emailSignature;
        mbp1.setContent(body, "text/html");

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);


        log.info("Number of attachments : {}", files.length);

        for (MultipartFile file : files) {

            log.info("Attaching " + file.getOriginalFilename() + "...");
            DataSource fds = new ByteArrayDataSource(file.getBytes(), file
                    .getContentType());
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(new DataHandler(fds));
            attachment.setFileName(file.getOriginalFilename());
            mp.addBodyPart(attachment);
            log.info(file.getOriginalFilename() + " attached");

        }
        message.setContent(mp);

        try {
            log.info("Sending email..");
            javaMailSender.send(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void sendMail(
            String toAddresses, String subject, String body) throws MessagingException {

        log.info("Sending email without attachment...");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);//true indicates multipart message
        helper.setSubject(subject);

        String[] toAddressList = toAddresses.split(",");

        for (String toAddress : toAddressList) {
            helper.addTo(new InternetAddress(toAddress));
        }
        log.info("Number of recipients : {}", toAddressList.length);
        helper.setText(body, true);//true indicates body is html

        MimeMultipart mp = new MimeMultipart();

        MimeBodyPart mbp1 = new MimeBodyPart();
        body = body + emailSignature;
        mbp1.setContent(body, "text/html");
        mp.addBodyPart(mbp1);

        message.setContent(mp);

        try {
            javaMailSender.send(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
