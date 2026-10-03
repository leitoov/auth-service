package com.nefetech.auth.infrastructure.config;

import com.nefetech.auth.application.LoginUseCase;
import com.nefetech.auth.application.ports.TokenGenerator;
import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.application.ports.AuditPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository, TokenGenerator tokenGenerator, AuditPort auditPort) {
        return new LoginUseCase(userRepository, tokenGenerator, auditPort);
    }
}
