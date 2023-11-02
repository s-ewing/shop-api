package com.example.shopapi.services;

import org.springframework.security.core.Authentication;

public interface TokenService {
    String generateJwt(Authentication auth, Long userId);
}
