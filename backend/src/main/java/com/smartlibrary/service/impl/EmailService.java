package com.smartlibrary.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String email, String token) {

        String link =
                "http://localhost:5173/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject("Reset Password");

        message.setText(
                "Click the link below to reset your password:\n\n"
                        + link
                        + "\n\nValid for 15 minutes."
        );

        mailSender.send(message);
    }

}