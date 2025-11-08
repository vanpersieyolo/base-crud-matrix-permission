package org.example.basespringbootproject.application.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestClazz {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("password"));

    }
}
