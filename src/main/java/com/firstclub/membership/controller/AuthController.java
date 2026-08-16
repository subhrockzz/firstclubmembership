package com.firstclub.membership.controller;

import com.firstclub.membership.dto.AuthRequest;
import com.firstclub.membership.dto.AuthResponse;
import com.firstclub.membership.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        // Mock authentication check
        if ((req.username().equals("vip-user") && req.password().equals("password")) ||
                (req.username().equals("admin") && req.password().equals("admin123"))) {
            String role = req.username().equals("admin") ? "ROLE_ADMIN" : "ROLE_USER";
            return ResponseEntity.ok(new AuthResponse(tokenProvider.generateToken(req.username(), role)));
        }
        return ResponseEntity.status(401).build();
    }
}