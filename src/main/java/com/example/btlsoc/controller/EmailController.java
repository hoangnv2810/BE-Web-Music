package com.example.btlsoc.controller;

import com.example.btlsoc.model.EmailMessage;
import com.example.btlsoc.model.User;
import com.example.btlsoc.repository.UserRepository;
import com.example.btlsoc.security.CustomUserDetailsService;
import com.example.btlsoc.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EmailController {
    private final EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send-email")
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage) {
        System.out.println(emailMessage.getMessage());
        User user = (User) customUserDetailsService.loadUserByEmail(emailMessage.getTo());
        if (user != null && !user.getProvider().equals("facebook") && !user.getProvider().equals("google")) {
            this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage() + " Mật khẩu mới là: 12345678");
            user.setPassword(passwordEncoder.encode("12345678"));
            userRepository.save(user);
            return ResponseEntity.ok("Success");
        }

        return ResponseEntity.badRequest().body("Email không tồn tại vui lòng thử lại");
    }
}
