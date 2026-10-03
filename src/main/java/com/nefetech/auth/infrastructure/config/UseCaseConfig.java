package com.nefetech.auth.infrastructure.config;

import com.nefetech.auth.application.LoginUseCase;
import com.nefetech.auth.application.RegisterBusinessUseCase;
import com.nefetech.auth.application.RegisterEmployeeUseCase;
import com.nefetech.auth.application.ports.PasswordHasherPort;
import com.nefetech.auth.application.ports.TenantRepository;
import com.nefetech.auth.application.ports.TokenGenerator;
import com.nefetech.auth.application.ports.UserRepository;
import com.nefetech.auth.application.ports.AuditPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository, TokenGenerator tokenGenerator, AuditPort auditPort, PasswordHasherPort passwordHasher) {
        return new LoginUseCase(userRepository, tokenGenerator, auditPort, passwordHasher);
    }

    @Bean
    public RegisterBusinessUseCase registerBusinessUseCase(TenantRepository tenantRepository, UserRepository userRepository, TokenGenerator tokenGenerator, PasswordHasherPort passwordHasher) {
        return new RegisterBusinessUseCase(tenantRepository, userRepository, tokenGenerator, passwordHasher);
    }

    @Bean
    public RegisterEmployeeUseCase registerEmployeeUseCase(UserRepository userRepository, PasswordHasherPort passwordHasher) {
        return new RegisterEmployeeUseCase(userRepository, passwordHasher);
    }
}
