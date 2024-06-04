package com.kjh.core.security;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class PasswordHasher extends Pbkdf2PasswordEncoder {

    private static final int SALT_LENGTH = 128;
    private static final int ITERATIONS = 100;

    public PasswordHasher(String secret) {
        super(secret, SALT_LENGTH, ITERATIONS, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }
}
