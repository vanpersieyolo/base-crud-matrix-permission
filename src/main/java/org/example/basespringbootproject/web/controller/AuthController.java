package org.example.basespringbootproject.web.controller;

import lombok.RequiredArgsConstructor;
import org.example.basespringbootproject.application.service.IAuthService;
import org.example.basespringbootproject.web.request.AuthRequest;
import org.example.basespringbootproject.web.request.AuthResponse;
import org.example.basespringbootproject.web.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        AuthResponse res = authService.login(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        AuthResponse res = authService.register(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String bearer) {
        if (bearer == null || !bearer.startsWith("Bearer ")) return ResponseEntity.badRequest().build();
        String token = bearer.substring(7);
        return ResponseEntity.ok(authService.refresh(token));
    }
}

