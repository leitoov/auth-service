package com.nefetech.auth.application.ports;

import com.nefetech.auth.domain.User;

public interface TokenGenerator {
    String generateToken(User user);
}
