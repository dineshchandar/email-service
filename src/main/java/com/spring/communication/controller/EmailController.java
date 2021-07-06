package com.spring.communication.controller;

import com.spring.communication.model.EmailRequest;
import com.spring.communication.util.SmtpMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Component
@RestController
@RequestMapping("/v1/communication")
public class EmailController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SmtpMailSender smtpMailSender;

    //Healthcheck
    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthCheck() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        log.info("Healthcheck");
        return new ResponseEntity<>("Server Time: " + dateFormat
                .format(System.currentTimeMillis()), HttpStatus.OK);
    }

    @PostMapping("/sendmailwithattach")
    public ResponseEntity<?> sendMailWithAttachment(
            @RequestPart(value = "file") MultipartFile[] file,
            @RequestPart("toEmailId") String toEmailId,
            @RequestPart("emailSubject") String emailSubject,
            @RequestPart("emailBody") String emailBody)
            throws MessagingException, IOException {

        smtpMailSender.sendMailWithAttachment(toEmailId, emailSubject, emailBody, file);

        return new ResponseEntity<>("Mail Sent with attachement", HttpStatus.OK);
    }

    @PostMapping("/sendmail")
    public ResponseEntity<?> sendMail(@RequestBody EmailRequest emailRequest)
            throws MessagingException {

        smtpMailSender.sendMail(emailRequest.getToEmailId(), emailRequest
                .getEmailSubject(), emailRequest.getEmailBody());

        return new ResponseEntity<>("Mail Sent", HttpStatus.OK);
    }
}
