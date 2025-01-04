package com.sparta.hotdeal.user.application.service;

public interface MailService {
    void sendEmail(String toEmail, String title, String text);
}
