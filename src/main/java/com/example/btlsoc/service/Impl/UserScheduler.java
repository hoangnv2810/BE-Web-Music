package com.example.btlsoc.service.Impl;

import com.example.btlsoc.model.AccountType;
import com.example.btlsoc.model.User;
import com.example.btlsoc.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;


@Component
public class UserScheduler {
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserScheduler.class);
    @Scheduled(cron = "0 0 * * * ?")
    public void updateExpiredVipUsers() {
       try {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           if(authentication.isAuthenticated()){
               List<User> vipUsers = userRepository.findByAllAccountVip();
               for (User user : vipUsers) {
                   if (user.getVipExpirationTime().isBefore(LocalDateTime.now())) {
                       user.setAccountType(AccountType.BASIC);
                       userRepository.save(user);
                   }
               }
               LOGGER.info("Check expiration time type account");
           }
       } catch (Exception e){
           System.out.println("Login");
       }
    }
}
