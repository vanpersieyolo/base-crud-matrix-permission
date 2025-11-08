package org.example.basespringbootproject.application.service;

import org.example.basespringbootproject.web.request.AuthRequest;
import org.example.basespringbootproject.web.request.AuthResponse;
import org.example.basespringbootproject.web.request.RegisterRequest;

public interface IAuthService {
    AuthResponse login(AuthRequest req);

    AuthResponse register(RegisterRequest req);

    AuthResponse refresh(String token);
}
