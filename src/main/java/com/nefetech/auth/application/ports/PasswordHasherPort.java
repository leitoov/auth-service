package com.nefetech.auth.application.ports;

public interface PasswordHasherPort {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
