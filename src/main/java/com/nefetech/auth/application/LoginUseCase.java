package com.nefetech.auth.application;

import com.nefetech.auth.domain.User;
import com.nefetech.auth.domain.exceptions.UserBlockedException;
import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.application.ports.TokenGenerator;
import com.nefetech.auth.application.ports.AuditPort;

public class LoginUseCase {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final AuditPort auditPort;

    public LoginUseCase(UserRepository userRepository, TokenGenerator tokenGenerator, AuditPort auditPort) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.auditPort = auditPort;
    }

    public String login(String email, String password, String deviceType) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    // Log asíncrono
                    auditPort.logLoginAttempt(email, "FAILED_USER_NOT_FOUND", "IP_DESCONOCIDA", deviceType);
                    return new RuntimeException("User not found");
                });

        if ("BLOCKED".equals(user.getStatus())) {
            auditPort.logLoginAttempt(email, "FAILED_USER_BLOCKED", "IP_DESCONOCIDA", deviceType);
            throw new UserBlockedException("El usuario se encuentra bloqueado.");
        }
        
        // TODO: Validar contraseñas reales usando BCrypt

        // Log exitoso asíncrono
        auditPort.logLoginAttempt(email, "SUCCESS", "IP_DESCONOCIDA", deviceType);
        
        return tokenGenerator.generateToken(user);
    }
}
