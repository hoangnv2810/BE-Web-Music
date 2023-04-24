package com.example.btlsoc.service;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);
}