package org.example.basespringbootproject.application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.basespringbootproject.application.service.IAuthService;
import org.example.basespringbootproject.domain.model.Role;
import org.example.basespringbootproject.domain.model.User;
import org.example.basespringbootproject.domain.repository.IRoleRepository;
import org.example.basespringbootproject.domain.repository.IUserRepository;
import org.example.basespringbootproject.infrastructure.security.jwt.JwtTokenProvider;
import org.example.basespringbootproject.web.request.AuthRequest;
import org.example.basespringbootproject.web.request.AuthResponse;
import org.example.basespringbootproject.web.request.RegisterRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    public AuthResponse login(AuthRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
            String token = tokenProvider.createToken(req.username());
            return new AuthResponse(token, "Bearer");
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUserName(req.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUserName(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setFullName(req.fullName());
        user.setActive(true);
        // default role USER if exists
        roleRepository.findByCode("USER").ifPresent(role -> {
            var roles = new HashSet<Role>();
            roles.add(role);
            user.setRoles(roles);
        });

        userRepository.save(user);
        String token = tokenProvider.createToken(user.getUserName());
        return new AuthResponse(token, "Bearer");
    }

    @Override
    public AuthResponse refresh(String token) {
        if (!tokenProvider.validateToken(token)) throw new IllegalArgumentException("Invalid token");
        String username = tokenProvider.getUsername(token);
        String newToken = tokenProvider.createToken(username);
        return new AuthResponse(newToken, "Bearer");
    }
}
