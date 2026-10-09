package com.smarthealth.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender sender;

    public void send(String to, String subject, String body) {
        try {
            SimpleMailMessage m = new SimpleMailMessage();
            m.setTo(to); m.setSubject(subject); m.setText(body); sender.send(m);
        } catch (Exception ignored) {}
    }

    public void sendWithPdf(String to, String subject, String body, byte[] pdf, String filename) {
        try {
            MimeMessage m = sender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(m, true, "UTF-8");
            h.setTo(to); h.setSubject(subject); h.setText(body);
            h.addAttachment(filename, new ByteArrayResource(pdf), "application/pdf");
            sender.send(m);
        } catch (Exception ignored) {}
    }
}
