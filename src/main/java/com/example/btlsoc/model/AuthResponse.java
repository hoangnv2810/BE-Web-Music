package com.example.btlsoc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {
    private String accessToken;
    private String username;
    private AccountType accountType;
    private String email;
}
