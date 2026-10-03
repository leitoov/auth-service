package com.nefetech.auth.application;

import com.nefetech.auth.domain.User;
import com.nefetech.auth.domain.exceptions.UserBlockedException;
import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.application.ports.TokenGenerator;
import com.nefetech.auth.application.ports.AuditPort;
import com.nefetech.auth.application.ports.PasswordHasherPort;

public class LoginUseCase {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final AuditPort auditPort;
    private final PasswordHasherPort passwordHasher;

    public LoginUseCase(UserRepository userRepository, TokenGenerator tokenGenerator, AuditPort auditPort, PasswordHasherPort passwordHasher) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.auditPort = auditPort;
        this.passwordHasher = passwordHasher;
    }

    public String login(String email, String password, String deviceType) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    auditPort.logLoginAttempt(email, "FAILED_USER_NOT_FOUND", "IP_DESCONOCIDA", deviceType);
                    return new RuntimeException("User not found");
                });

        if ("BLOCKED".equals(user.getStatus())) {
            auditPort.logLoginAttempt(email, "FAILED_USER_BLOCKED", "IP_DESCONOCIDA", deviceType);
            throw new UserBlockedException("El usuario se encuentra bloqueado.");
        }
        
        if (!passwordHasher.matches(password, user.getPassword())) {
            auditPort.logLoginAttempt(email, "FAILED_BAD_CREDENTIALS", "IP_DESCONOCIDA", deviceType);
            throw new RuntimeException("Credenciales inválidas");
        }

        auditPort.logLoginAttempt(email, "SUCCESS", "IP_DESCONOCIDA", deviceType);
        
        return tokenGenerator.generateToken(user);
    }
}
