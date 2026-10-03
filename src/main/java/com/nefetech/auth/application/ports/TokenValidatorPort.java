package com.nefetech.auth.application.ports;

import java.util.Map;

public interface TokenValidatorPort {
    Map<String, Object> extractClaims(String token);
}
